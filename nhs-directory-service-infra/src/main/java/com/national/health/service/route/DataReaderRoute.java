package com.national.health.service.route;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.national.health.service.aggregator.DataAggregator;
import com.national.health.service.converter.RestDomainConverter;
import com.national.health.service.model.RestAdmission;
import com.national.health.service.model.RestPatient;
import com.national.health.service.processor.DataProcessor;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.dataformat.CsvDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import static com.national.health.service.constants.InfraConstants.GENERIC_INFRA_ROUTE_ERROR_MESSAGE;

/**
 * DataReaderRouter class processing camel routes
 */
public class DataReaderRoute extends RouteBuilder {

    @Autowired
    private DataProcessor dataProcessor;

    @Autowired
    private RestDomainConverter restDomainConverter;

    @Override
    public void configure() throws Exception {
        JacksonDataFormat patientDataFormat = new JacksonDataFormat(RestPatient.class);
        patientDataFormat.useList();
        patientDataFormat.setInclude(JsonInclude.Include.NON_EMPTY.name());
        patientDataFormat.setUnmarshalType(RestPatient.class);

        JacksonDataFormat admissionDataFormat = new JacksonDataFormat(RestAdmission.class);
        admissionDataFormat.useList();
        admissionDataFormat.setInclude(JsonInclude.Include.NON_EMPTY.name());
        admissionDataFormat.setUnmarshalType(RestAdmission.class);

        CsvDataFormat csv = new CsvDataFormat();
        csv.setLazyLoad(true);
        csv.setUseMaps(true);

        /**
         * Route handling generic Exception
         */
        onException(Exception.class)
                .routeId("EXCEPTION_PROCESS_ROUTE")
                .log("Reached Exception Processor")
                .log(GENERIC_INFRA_ROUTE_ERROR_MESSAGE + "${body}")
                .continued(true)
        ;

        /**
         * Route getting triggered on file drop on data_drop folder location
         * Reads file by splitting,aggregates and process based on file type
         */
        from("file://data_drop")
                .routeId("DATA_READER_ROUTE")
                .to("direct:auditRoute")
                .unmarshal(csv)
                .split(body()).streaming()
                .aggregate(constant(true), new DataAggregator())
                .completionSize(100)
                .completionTimeout(6000)
                .choice()
                .when(isProperFileRead("admission"))
                .to("direct:AdmissionDataProcessor")
                .when(isProperFileRead("patient"))
                .to("direct:PatientDataProcessor")
                .endChoice()
        ;

        /**
         * Route processing Patient Data file
         * Converting JSON data to Rest Patient Model and process
         */
        from("direct:PatientDataProcessor")
                .routeId("PATIENT_DATA_PROCESSOR_ROUTE")
                .log("Reached PatientDataProcessor")
                .marshal().json(JsonLibrary.Jackson, true)
                .convertBodyTo(String.class)
                .unmarshal(patientDataFormat)
                .to("bean:restToDomainConverter?method=restPatientToDomainPatient(${body})")
                .to("bean:dataProcessor?method=patientDataProcessor(${body})")
        ;

        /**
         * Route processing Admission Data file
         * Converting JSON data to Rest Admission Model and process
         */
        from("direct:AdmissionDataProcessor")
                .routeId("ADMISSION_DATA_PROCESSOR_ROUTE")
                .log("Reached AdmissionDataProcessor")
                .marshal().json(JsonLibrary.Jackson, true)
                .convertBodyTo(String.class)
                .unmarshal(admissionDataFormat)
                .to("bean:restToDomainConverter?method=restAdmissionToDomainAdmission(${body})")
                .to("bean:dataProcessor?method=admissionDataProcessor(${body})")
        ;

        /* Todo Implement Audit Route */
        from("direct:auditRoute")
                .routeId("AUDIT_ROUTE")
                .log("Reached Auditing File Data Route")
        ;

    }

    @NotNull
    private Predicate isProperFileRead(String fileNamePrefix) {
        return exchange -> exchange.getIn().getHeader("CamelFileNameConsumed").toString().startsWith(fileNamePrefix);
    }

}

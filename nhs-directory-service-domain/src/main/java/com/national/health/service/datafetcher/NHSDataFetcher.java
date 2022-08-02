package com.national.health.service.datafetcher;

import com.national.health.service.model.NHSData;
import com.national.health.service.repository.MysqlRepository;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.national.health.service.constants.DomainConstants.ADMISSION_END_DATE_LIST;
import static com.national.health.service.constants.DomainConstants.ADMISSION_OUT_COME_LIST;
import static com.national.health.service.constants.DomainConstants.ADMISSION_SOURCE_LIST;
import static com.national.health.service.constants.DomainConstants.ADMISSION_START_DATE_LIST;
import static com.national.health.service.constants.DomainConstants.ETHNICITY_LIST;
import static com.national.health.service.constants.DomainConstants.OFFSET;
import static com.national.health.service.constants.DomainConstants.PATIENT_ID_LIST;
import static com.national.health.service.constants.DomainConstants.SEX_AT_BIRTH_LIST;
import static com.national.health.service.constants.DomainConstants.YEAR_OF_BIRTH_LIST;

/**
 * NHSDataFetcher class implements Graphql DataFetcher where in
 * it provides option to extract the schema arguments from {@link DataFetchingEnvironment}
 */
@Component
public class NHSDataFetcher implements DataFetcher<List<NHSData>> {

    @Autowired
    private MysqlRepository mysqlRepository;

    /**
     * Method provides extracted schema arguments in the form of {@link NHSData} list
     *
     * @param dataFetchingEnvironment :   DataFetchingEnvironment Object with environment values
     * @return {@link List<NHSData>}
     */
    @Override
    public List<NHSData> get(DataFetchingEnvironment dataFetchingEnvironment) {
        Map<String, List<String>> nhsDataArgumentsMap = new LinkedHashMap<>();
        nhsDataArgumentsMap.put(PATIENT_ID_LIST, dataFetchingEnvironment.getArgument(PATIENT_ID_LIST));
        nhsDataArgumentsMap.put(YEAR_OF_BIRTH_LIST, dataFetchingEnvironment.getArgument(YEAR_OF_BIRTH_LIST));
        nhsDataArgumentsMap.put(SEX_AT_BIRTH_LIST, dataFetchingEnvironment.getArgument(SEX_AT_BIRTH_LIST));
        nhsDataArgumentsMap.put(ETHNICITY_LIST, dataFetchingEnvironment.getArgument(ETHNICITY_LIST));
        nhsDataArgumentsMap.put(ADMISSION_START_DATE_LIST, dataFetchingEnvironment.getArgument(ADMISSION_START_DATE_LIST));
        nhsDataArgumentsMap.put(ADMISSION_END_DATE_LIST, dataFetchingEnvironment.getArgument(ADMISSION_END_DATE_LIST));
        nhsDataArgumentsMap.put(ADMISSION_SOURCE_LIST, dataFetchingEnvironment.getArgument(ADMISSION_SOURCE_LIST));
        nhsDataArgumentsMap.put(ADMISSION_OUT_COME_LIST, dataFetchingEnvironment.getArgument(ADMISSION_OUT_COME_LIST));
        Long offsetArgument = Long.parseLong(dataFetchingEnvironment.getArgument(OFFSET));
        return mysqlRepository.findNHSData(nhsDataArgumentsMap, offsetArgument);
    }
}

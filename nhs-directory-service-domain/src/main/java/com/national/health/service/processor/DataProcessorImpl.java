package com.national.health.service.processor;

import com.national.health.service.model.DomainAdmission;
import com.national.health.service.model.DomainPatient;
import com.national.health.service.repository.MysqlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * DataProcessorImpl implementing DataProcessor interface
 * handling domain data list
 */
@Component
public class DataProcessorImpl implements DataProcessor {

    @Autowired
    private MysqlRepository mysqlRepository;

    /**
     * Method processing Domain Patient data list
     *
     * @param domainPatientList :   List of domain patients
     */
    @Override
    public void patientDataProcessor(List<DomainPatient> domainPatientList) {
        mysqlRepository.savePatientListData(domainPatientList);
    }

    /**
     * Method processing Domain Admission data list
     *
     * @param domainAdmissionList :  List of domain admission
     */
    @Override
    public void admissionDataProcessor(List<DomainAdmission> domainAdmissionList) {
        mysqlRepository.saveAdmissionListData(domainAdmissionList);
    }
}

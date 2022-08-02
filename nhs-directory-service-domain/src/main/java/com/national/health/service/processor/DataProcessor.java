package com.national.health.service.processor;

import com.national.health.service.model.DomainAdmission;
import com.national.health.service.model.DomainPatient;

import java.util.List;

/**
 * Interface for processing Domain Data from Infra layer
 */
public interface DataProcessor {
    /**
     * Method processing Domain Patient Data
     *
     * @param domainPatientList :   List of domain patients
     */
    void patientDataProcessor(List<DomainPatient> domainPatientList);

    /**
     * Method processing Domain Admission Data
     *
     * @param domainAdmissionList :  List of domain admission
     */
    void admissionDataProcessor(List<DomainAdmission> domainAdmissionList);
}

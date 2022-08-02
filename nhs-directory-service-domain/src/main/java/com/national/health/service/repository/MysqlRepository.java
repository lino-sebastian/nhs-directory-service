package com.national.health.service.repository;

import com.national.health.service.model.DomainAdmission;
import com.national.health.service.model.DomainPatient;
import com.national.health.service.model.NHSData;

import java.util.List;
import java.util.Map;

/**
 * Interface for handling Domain data DB operations by Infra layer
 */
public interface MysqlRepository {
    /**
     * Method handling save operation of Domain patient list
     *
     * @param domainPatientList : List od Domain Patients
     */
    void savePatientListData(List<DomainPatient> domainPatientList);

    /**
     * Method handling save operation of Domain admission list
     *
     * @param domainAdmissionList :   : List od Domain Admissions
     */
    void saveAdmissionListData(List<DomainAdmission> domainAdmissionList);

    /**
     * Method handling find operation of Graphql NHSData
     *
     * @param nhsDataArgumentsMap :   Map of Graphql schema arguments
     * @param offsetArgument      :   Query offset argument
     * @return {@link List<NHSData>}
     */
    List<NHSData> findNHSData(Map<String, List<String>> nhsDataArgumentsMap, Long offsetArgument);
}

package com.national.health.service.converter;

import com.national.health.service.model.DBAdmission;
import com.national.health.service.model.DBPatient;
import com.national.health.service.model.DomainAdmission;
import com.national.health.service.model.DomainPatient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Converter class converting models between Domain and DB Layer(Infra)
 */
@Component
public class DBDomainConverter {

    /**
     * Method converting list of domain Patients to DB Patients
     *
     * @param domainPatientList :     List of domain Patients
     * @return {@link List<DBPatient>}
     */
    public List<DBPatient> domainPatientToDBPatient(List<DomainPatient> domainPatientList) {
        return domainPatientList.stream()
                .filter(Objects::nonNull)
                .map(domainPatientToDBPatientFunction)
                .collect(Collectors.toList());
    }

    /**
     * Method converting list of db Patients to Domain Patients
     *
     * @param dbPatientList :    List of db Patients
     * @return {@link List<DomainPatient>}
     */
    public List<DomainPatient> dbPatientToDomainPatient(List<DBPatient> dbPatientList) {
        return dbPatientList.stream()
                .filter(Objects::nonNull)
                .map(dbPatientToDomainPatientFunction)
                .collect(Collectors.toList());
    }

    /**
     * Method converting list of domain Admission to DB Admission
     *
     * @param domainAdmissionList :   List of Domain Admission
     * @return {@link List<DBAdmission>}
     */
    public List<DBAdmission> domainAdmissionToDBAdmission(List<DomainAdmission> domainAdmissionList) {
        return domainAdmissionList.stream()
                .filter(Objects::nonNull)
                .map(domainAdmissionToDBAdmissionFunction)
                .collect(Collectors.toList());
    }

    /**
     * Functional Interface accepting Domain Patient lambda and builds DB Patient from it
     */
    Function<DomainPatient, DBPatient> domainPatientToDBPatientFunction = domainPatient -> DBPatient.builder()
            .patientId(domainPatient.getPatientId())
            .fullName(domainPatient.getFullName())
            .sexAtBirth(domainPatient.getSexAtBirth())
            .ethnicity(domainPatient.getEthnicity())
            .yearOfBirth(domainPatient.getYearOfBirth())
            .monthOfBirth(domainPatient.getMonthOfBirth())
            .dayOfBirth(domainPatient.getDayOfBirth())
            .birthDateTime(domainPatient.getBirthDateTime())
            .deathDateTime(domainPatient.getDeathDateTime())
            .build();

    /**
     * Functional Interface accepting DB Patient lambda and builds Domain Patient from it
     */
    Function<DBPatient, DomainPatient> dbPatientToDomainPatientFunction = dbPatient -> DomainPatient.builder()
            .patientId(dbPatient.getPatientId())
            .fullName(dbPatient.getFullName())
            .sexAtBirth(dbPatient.getSexAtBirth())
            .ethnicity(dbPatient.getEthnicity())
            .yearOfBirth(dbPatient.getYearOfBirth())
            .monthOfBirth(dbPatient.getMonthOfBirth())
            .dayOfBirth(dbPatient.getDayOfBirth())
            .birthDateTime(dbPatient.getBirthDateTime())
            .deathDateTime(dbPatient.getDeathDateTime())
            .build();

    /**
     * Functional Interface accepting Domain Admission lambda and builds DB Admission from it
     */
    Function<DomainAdmission, DBAdmission> domainAdmissionToDBAdmissionFunction = domainAdmission -> DBAdmission.builder()
            .admissionId(domainAdmission.getAdmissionId())
            .patientId(domainAdmission.getPatientId())
            .visitStartDate(domainAdmission.getVisitStartDate())
            .visitStartDateTime(domainAdmission.getVisitStartDateTime())
            .visitEndDate(domainAdmission.getVisitEndDate())
            .visitEndDateTime(domainAdmission.getVisitEndDateTime())
            .admissionSource(domainAdmission.getAdmissionSource())
            .dischargeTo(domainAdmission.getDischargeTo())
            .build();
}

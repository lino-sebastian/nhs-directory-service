package com.national.health.service.converter;

import com.national.health.service.model.DomainAdmission;
import com.national.health.service.model.DomainPatient;
import com.national.health.service.model.RestAdmission;
import com.national.health.service.model.RestPatient;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

/**
 * Converter class converting models between Rest(Infra) and Domain Layer
 */
public class RestDomainConverter {

    /**
     * Method converting list of rest Patients to List of Domain Patients
     *
     * @param restPatientList :       List of RestPatients
     * @return {@link List<DomainPatient>}
     */
    public List<DomainPatient> restPatientToDomainPatient(List<RestPatient> restPatientList) {
        return restPatientList.stream()
                .filter(Objects::nonNull)
                .map(restPatientToDomainPatientFunction)
                .collect(Collectors.toList());
    }

    /**
     * Method converting list of rest Admission to List of Domain Admission
     *
     * @param restAdmissionList :       List of RestAdmission
     * @return {@link List<DomainAdmission>}
     */
    public List<DomainAdmission> restAdmissionToDomainAdmission(List<RestAdmission> restAdmissionList) {
        return restAdmissionList.stream()
                .filter(Objects::nonNull)
                .map(restAdmissionToDomainAdmissionFunction)
                .collect(Collectors.toList());
    }

    /**
     * Functional Interface accepting rest Patient lambda and builds Domain Patient from it.
     * Filtering out restPatient lambda(object) without patientId
     */
    Function<RestPatient, DomainPatient> restPatientToDomainPatientFunction = restPatient -> {
        if (nonNull(restPatient.getPatientId()))
            return DomainPatient.builder()
                    .patientId(restPatient.getPatientId())
                    .fullName(restPatient.getFullName())
                    .sexAtBirth(restPatient.getSexAtBirth())
                    .ethnicity(restPatient.getEthnicity())
                    .yearOfBirth(restPatient.getYearOfBirth())
                    .monthOfBirth(restPatient.getMonthOfBirth())
                    .dayOfBirth(restPatient.getDayOfBirth())
                    .birthDateTime(restPatient.getBirthDateTime())
                    .deathDateTime(restPatient.getDeathDateTime())
                    .build();
        return null;
    };

    /**
     * Functional Interface accepting rest Admission lambda and builds Domain Admission from it.
     * Filtering out restAdmission lambda(object) without admissionId
     */
    Function<RestAdmission, DomainAdmission> restAdmissionToDomainAdmissionFunction = restAdmission -> {
        if (nonNull(restAdmission.getAdmissionId()))
            return DomainAdmission.builder()
                    .admissionId(restAdmission.getAdmissionId())
                    .patientId(restAdmission.getPatientId())
                    .visitStartDate(restAdmission.getVisitStartDate())
                    .visitStartDateTime(restAdmission.getVisitStartDateTime())
                    .visitEndDate(restAdmission.getVisitEndDate())
                    .visitEndDateTime(restAdmission.getVisitEndDateTime())
                    .admissionSource(restAdmission.getAdmissionSource())
                    .dischargeTo(restAdmission.getDischargeTo())
                    .build();
        return null;
    };
}


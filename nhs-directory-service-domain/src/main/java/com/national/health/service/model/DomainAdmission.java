package com.national.health.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Domain Model class for handling Admission data
 */
@Data
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DomainAdmission {
    private Long admissionId;
    private Long patientId;
    private String visitStartDate;
    private String visitStartDateTime;
    private String visitEndDate;
    private String visitEndDateTime;
    private String admissionSource;
    private String dischargeTo;
}

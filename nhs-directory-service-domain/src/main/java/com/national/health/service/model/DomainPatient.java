package com.national.health.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Domain Model class for handling Patient data
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DomainPatient {
    private Long patientId;
    private String fullName;
    private String sexAtBirth;
    private String ethnicity;
    private Long yearOfBirth;
    private Long monthOfBirth;
    private Long dayOfBirth;
    private String birthDateTime;
    private String deathDateTime;
}

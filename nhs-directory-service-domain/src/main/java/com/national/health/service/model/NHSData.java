package com.national.health.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Domain Model class for handling and sending Graphql NHS data
 */
@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NHSData {
    private String ethnicity;
    private String patientId;
    private Integer yearOfBirth;
    private String sexAtBirth;
    private String admissionStartDateTime;
    private String admissionEndDateTime;
    private String admissionSource;
    private String admissionOutcome;
    private Long offSet;
}

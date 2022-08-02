package com.national.health.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model class for Rest layer(Infra) to perform Rest operations
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class RestPatient {
    @JsonProperty(value = "patient_id")
    private Long patientId;
    @JsonProperty(value = "full_name")
    private String fullName;
    @JsonProperty(value = "sex_at_birth")
    private String sexAtBirth;
    @JsonProperty(value = "ethnicity")
    private String ethnicity;
    @JsonProperty(value = "year_of_birth")
    private Long yearOfBirth;
    @JsonProperty(value = "month_of_birth")
    private Long monthOfBirth;
    @JsonProperty(value = "day_of_birth")
    private Long dayOfBirth;
    @JsonProperty(value = "birth_datetime")
    private String birthDateTime;
    @JsonProperty(value = "death_datetime")
    private String deathDateTime;
}

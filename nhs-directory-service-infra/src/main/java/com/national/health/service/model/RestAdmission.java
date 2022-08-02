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
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestAdmission {
    @JsonProperty("admission_id")
    private Long admissionId;
    @JsonProperty("patient_id")
    private Long patientId;
    @JsonProperty("visit_start_date")
    private String visitStartDate;
    @JsonProperty("visit_start_datetime")
    private String visitStartDateTime;
    @JsonProperty("visit_end_date")
    private String visitEndDate;
    @JsonProperty("visit_end_datetime")
    private String visitEndDateTime;
    @JsonProperty("admission_source")
    private String admissionSource;
    @JsonProperty("discharge_to")
    private String dischargeTo;
}

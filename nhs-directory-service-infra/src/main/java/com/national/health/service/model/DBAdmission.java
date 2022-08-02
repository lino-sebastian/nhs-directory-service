package com.national.health.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Model class for DB layer(Infra) to perform DB operations
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "nhs_admission")
public class DBAdmission {
    @Id
    @Column(name = "admission_id")
    private Long admissionId;
    @Column(name = "patient_id")
    private Long patientId;
    @Column(name = "visit_start_date")
    private String visitStartDate;
    @Column(name = "visit_start_datetime")
    private String visitStartDateTime;
    @Column(name = "visit_end_date")
    private String visitEndDate;
    @Column(name = "visit_end_datetime")
    private String visitEndDateTime;
    @Column(name = "admission_source")
    private String admissionSource;
    @Column(name = "discharge_to")
    private String dischargeTo;
}

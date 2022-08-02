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
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Table(name = "nhs_patient")
public class DBPatient {
    @Id
    @Column(name = "patient_id")
    private Long patientId;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "sex_at_birth")
    private String sexAtBirth;
    @Column(name = "ethnicity")
    private String ethnicity;
    @Column(name = "year_of_birth")
    private Long yearOfBirth;
    @Column(name = "month_of_birth")
    private Long monthOfBirth;
    @Column(name = "day_of_birth")
    private Long dayOfBirth;
    @Column(name = "birth_datetime")
    private String birthDateTime;
    @Column(name = "death_datetime")
    private String deathDateTime;
}

package com.national.health.service.repository;

import com.national.health.service.model.DBPatient;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface for PatientRepository to perform DBPatient DB operations
 */
public interface PatientRepository extends JpaRepository<DBPatient, Long> {
}

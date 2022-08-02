package com.national.health.service.repository;

import com.national.health.service.model.DBAdmission;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface for AdmissionRepository to perform DBAdmission DB operations
 */
public interface AdmissionRepository extends JpaRepository<DBAdmission, Long> {
}

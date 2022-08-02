CREATE TABLE IF NOT EXISTS nhs_patient (
    patient_id VARCHAR(50) PRIMARY KEY,
    full_name VARCHAR(50),
    sex_at_birth VARCHAR(10),
    ethnicity VARCHAR(50),
    year_of_birth INT,
    month_of_birth INT,
    day_of_birth INT,
    birth_datetime DATETIME,
    death_datetime DATETIME
);
CREATE TABLE IF NOT EXISTS nhs_admission (
    admission_id VARCHAR(50) PRIMARY KEY,
    patient_id VARCHAR(50),
    visit_start_date DATE,
    visit_start_datetime DATETIME,
    visit_end_date DATE,
    visit_end_datetime DATETIME,
    admission_source VARCHAR(50),
    discharge_to VARCHAR(50)
);
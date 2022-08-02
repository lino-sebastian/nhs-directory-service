package com.national.health.service.constants;

/**
 * Final Constant class for Infra Layer
 */
public final class InfraConstants {
    private InfraConstants() {
    }

    // Query display fields
    public static final String SELECT_STATEMENT = "select ";
    public static final String WHERE_STATEMENT = " where ";
    public static final String LIKE_STATEMENT = " like ";
    public static final String AND_STATEMENT = " and ";
    public static final String OR_STATEMENT = " or ";
    public static final String BETWEEN_STATEMENT = " between ";
    public static final String LIMIT_STATEMENT = " order by admission.patient_id limit ";
    public static final String OFFSET_STATEMENT = " offset ";
    public static final String JOIN_STATEMENT = " from nhs_admission admission inner join nhs_patient patient on ";
    public static final String ADMISSION_ID_FIELD = "admission.patient_id";
    public static final String PATIENT_ID_FIELD = "patient.patient_id";
    public static final String PATIENT_YEAR_OF_BIRTH_FIELD = "patient.year_of_birth";
    public static final String PATIENT_SEX_AT_BIRTH_FIELD = "patient.sex_at_birth";
    public static final String PATIENT_ETHNICITY_FIELD = "patient.ethnicity";
    public static final String ADMISSION_START_DATE_FIELD = "admission.visit_start_date";
    public static final String ADMISSION_END_DATE_FIELD = "admission.visit_end_date";
    public static final String ADMISSION_SOURCE_FIELD = "admission.admission_source";
    public static final String ADMISSION_OUTCOME_FIELD = "admission.discharge_to";
    public static final String FIELD_DELIMITER = " , ";
    public static final String EQUALITY_DELIMITER = " = ";
    public static final String PREPARED_STATEMENT_DELIMITER = "?";
    public static final String MATCHING_DELIMITER = "%";
    public static final String FUNCTION_OPEN_BRACES = "(";
    public static final String FUNCTION_CLOSE_BRACES = ")";
    public static final String JOIN_EQUALITY_STATEMENT = ADMISSION_ID_FIELD + EQUALITY_DELIMITER + PATIENT_ID_FIELD;
    // Prepare statements fields
    public static final String QUERY_PARAM_PATIENT_ID = "patientId";
    public static final String QUERY_PARAM_YEAR_OF_BIRTH_START = "yearOfBirthStart";
    public static final String QUERY_PARAM_YEAR_OF_BIRTH_END = "yearOfBirthEnd";
    public static final String QUERY_PARAM_SEX_AT_BIRTH = "sexAtBirth";
    public static final String QUERY_PARAM_ETHNICITY = "ethnicity";
    public static final String QUERY_PARAM_ADMISSION_START_DATE_RANGE_START = "admissionStartDateRangeStart";
    public static final String QUERY_PARAM_ADMISSION_START_DATE_RANGE_END = "admissionStartDateRangeEnd";
    public static final String QUERY_PARAM_ADMISSION_END_DATE_RANGE_START = "admissionEndDateRangeStart";
    public static final String QUERY_PARAM_ADMISSION_END_DATE_RANGE_END = "admissionEndDateRangeEnd";
    public static final String QUERY_PARAM_ADMISSION_SOURCE = "admissionSource";
    public static final String QUERY_PARAM_ADMISSION_OUTCOME = "admissionOutCome";

    public static final String GENERIC_INFRA_ERROR_MESSAGE = "Error Occurred : ";
    public static final String GENERIC_INFRA_ROUTE_ERROR_MESSAGE = "Route Error Occurred : ";

}

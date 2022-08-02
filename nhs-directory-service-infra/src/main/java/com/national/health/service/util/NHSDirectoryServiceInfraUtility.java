package com.national.health.service.util;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.persistence.Query;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.national.health.service.constants.DomainConstants.ADMISSION_END_DATE_LIST;
import static com.national.health.service.constants.DomainConstants.ADMISSION_OUT_COME_LIST;
import static com.national.health.service.constants.DomainConstants.ADMISSION_SOURCE_LIST;
import static com.national.health.service.constants.DomainConstants.ADMISSION_START_DATE_LIST;
import static com.national.health.service.constants.DomainConstants.ETHNICITY_LIST;
import static com.national.health.service.constants.DomainConstants.PATIENT_ID_LIST;
import static com.national.health.service.constants.DomainConstants.SEX_AT_BIRTH_LIST;
import static com.national.health.service.constants.DomainConstants.YEAR_OF_BIRTH_LIST;
import static com.national.health.service.constants.InfraConstants.ADMISSION_END_DATE_FIELD;
import static com.national.health.service.constants.InfraConstants.ADMISSION_OUTCOME_FIELD;
import static com.national.health.service.constants.InfraConstants.ADMISSION_SOURCE_FIELD;
import static com.national.health.service.constants.InfraConstants.ADMISSION_START_DATE_FIELD;
import static com.national.health.service.constants.InfraConstants.AND_STATEMENT;
import static com.national.health.service.constants.InfraConstants.BETWEEN_STATEMENT;
import static com.national.health.service.constants.InfraConstants.EQUALITY_DELIMITER;
import static com.national.health.service.constants.InfraConstants.FIELD_DELIMITER;
import static com.national.health.service.constants.InfraConstants.FUNCTION_CLOSE_BRACES;
import static com.national.health.service.constants.InfraConstants.FUNCTION_OPEN_BRACES;
import static com.national.health.service.constants.InfraConstants.LIKE_STATEMENT;
import static com.national.health.service.constants.InfraConstants.MATCHING_DELIMITER;
import static com.national.health.service.constants.InfraConstants.OR_STATEMENT;
import static com.national.health.service.constants.InfraConstants.PATIENT_ETHNICITY_FIELD;
import static com.national.health.service.constants.InfraConstants.PATIENT_ID_FIELD;
import static com.national.health.service.constants.InfraConstants.PATIENT_SEX_AT_BIRTH_FIELD;
import static com.national.health.service.constants.InfraConstants.PATIENT_YEAR_OF_BIRTH_FIELD;
import static com.national.health.service.constants.InfraConstants.PREPARED_STATEMENT_DELIMITER;
import static com.national.health.service.constants.InfraConstants.QUERY_PARAM_ADMISSION_END_DATE_RANGE_END;
import static com.national.health.service.constants.InfraConstants.QUERY_PARAM_ADMISSION_END_DATE_RANGE_START;
import static com.national.health.service.constants.InfraConstants.QUERY_PARAM_ADMISSION_OUTCOME;
import static com.national.health.service.constants.InfraConstants.QUERY_PARAM_ADMISSION_SOURCE;
import static com.national.health.service.constants.InfraConstants.QUERY_PARAM_ADMISSION_START_DATE_RANGE_END;
import static com.national.health.service.constants.InfraConstants.QUERY_PARAM_ADMISSION_START_DATE_RANGE_START;
import static com.national.health.service.constants.InfraConstants.QUERY_PARAM_ETHNICITY;
import static com.national.health.service.constants.InfraConstants.QUERY_PARAM_PATIENT_ID;
import static com.national.health.service.constants.InfraConstants.QUERY_PARAM_SEX_AT_BIRTH;
import static com.national.health.service.constants.InfraConstants.QUERY_PARAM_YEAR_OF_BIRTH_END;
import static com.national.health.service.constants.InfraConstants.QUERY_PARAM_YEAR_OF_BIRTH_START;
import static com.national.health.service.constants.InfraConstants.WHERE_STATEMENT;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Utility class handling Infra utility functionalities
 */
@Component
public class NHSDirectoryServiceInfraUtility {

    /**
     * Method constructing conditional Query statements
     *
     * @param nhsDataArgumentsMap            :   Graphql schema arguments map
     * @param preparedStatementsParameterMap :   Prepare statements parameter map
     * @return {@link String}
     */
    public String getConditionalQueryStatements(
            Map<String, List<String>> nhsDataArgumentsMap,
            Map<String, Object> preparedStatementsParameterMap
    ) {
        StringBuilder conditionalQueryStatementsBuilder = new StringBuilder();
        conditionalQueryStatementsBuilder.append(WHERE_STATEMENT);
        nhsDataArgumentsMap.entrySet()
                .forEach(entry -> {
                    String key = Optional.ofNullable(entry).map(Map.Entry::getKey).orElse("");
                    if (key.equals(PATIENT_ID_LIST)) {
                        generateConditionalStatementForPatientIdList(preparedStatementsParameterMap, conditionalQueryStatementsBuilder, entry);
                    } else if (key.equals(YEAR_OF_BIRTH_LIST)) {
                        generateConditionalStatementForYearOfBirthList(preparedStatementsParameterMap, conditionalQueryStatementsBuilder, entry);
                    } else if (key.equals(SEX_AT_BIRTH_LIST)) {
                        generateConditionalStatementForSexAtBirthList(preparedStatementsParameterMap, conditionalQueryStatementsBuilder, entry);
                    } else if (key.equals(ETHNICITY_LIST)) {
                        generateConditionalStatementForEthnicityList(preparedStatementsParameterMap, conditionalQueryStatementsBuilder, entry);

                    } else if (key.equals(ADMISSION_START_DATE_LIST)) {
                        generateConditionalStatementForAdmissionStartDateList(preparedStatementsParameterMap, conditionalQueryStatementsBuilder, entry);

                    } else if (key.equals(ADMISSION_END_DATE_LIST)) {
                        generateConditionalStatementForAdmissionEndDateList(preparedStatementsParameterMap, conditionalQueryStatementsBuilder, entry);

                    } else if (key.equals(ADMISSION_SOURCE_LIST)) {
                        generateConditionalStatementForAdmissionSourceList(preparedStatementsParameterMap, conditionalQueryStatementsBuilder, entry);
                    } else if (key.equals(ADMISSION_OUT_COME_LIST)) {
                        generateConditionalStatementForAdmissionOutcomeList(preparedStatementsParameterMap, conditionalQueryStatementsBuilder, entry);
                    }
                });
        String conditionalQueryStatement = conditionalQueryStatementsBuilder.toString();
        if (conditionalQueryStatement.endsWith(AND_STATEMENT)) {
            return getTrimmedConditionalStatement(conditionalQueryStatement);
        }
        return conditionalQueryStatement;
    }

    /**
     * Method to remove trailing SQl statements from the dynamic query
     *
     * @param conditionalQueryStatement :   conditionalQueryStatement
     * @return {@link String}
     */
    @NotNull
    private String getTrimmedConditionalStatement(String conditionalQueryStatement) {
        return conditionalQueryStatement.substring(0, conditionalQueryStatement.length() - AND_STATEMENT.length());
    }

    /**
     * Method appending AdmissionOutcome conditional statement builder based on its occurrence in nhsDataArgumentsMap
     * Also populating prepare statements using preparedStatementsParameterMap
     *
     * @param preparedStatementsParameterMap    :   preparedStatementsParameterMap
     * @param conditionalQueryStatementsBuilder :   conditionalQueryStatements String Builder
     * @param nhsDataArgumentsMap               :   Graphql schema arguments map
     */
    private void generateConditionalStatementForAdmissionOutcomeList(
            Map<String, Object> preparedStatementsParameterMap,
            StringBuilder conditionalQueryStatementsBuilder,
            Map.Entry<String, List<String>> nhsDataArgumentsMap
    ) {
        List<String> admissionOutComeList = Optional.of(nhsDataArgumentsMap).map(Map.Entry::getValue).orElse(Collections.emptyList());
        long admissionOutComeListCount = admissionOutComeList.stream().filter(Objects::nonNull).count();
        if (admissionOutComeListCount > 0) {
            conditionalQueryStatementsBuilder.append(FUNCTION_OPEN_BRACES);

            for (int i = 0; i < admissionOutComeListCount; i++) {
                conditionalQueryStatementsBuilder
                        .append(ADMISSION_OUTCOME_FIELD)
                        .append(EQUALITY_DELIMITER)
                        .append(PREPARED_STATEMENT_DELIMITER);
                if (i < admissionOutComeListCount - 1) {
                    conditionalQueryStatementsBuilder.append(OR_STATEMENT);
                }
            }
            conditionalQueryStatementsBuilder.append(FUNCTION_CLOSE_BRACES);
        }
        preparedStatementsParameterMap.put(QUERY_PARAM_ADMISSION_OUTCOME, admissionOutComeList);
    }

    /**
     * Method appending AdmissionSource conditional statement builder based on its occurrence in nhsDataArgumentsMap
     * Also populating prepare statements using preparedStatementsParameterMap
     *
     * @param preparedStatementsParameterMap    :   preparedStatementsParameterMap
     * @param conditionalQueryStatementsBuilder :   conditionalQueryStatements String Builder
     * @param nhsDataArgumentsMap               :   Graphql schema arguments map
     */
    private void generateConditionalStatementForAdmissionSourceList(
            Map<String, Object> preparedStatementsParameterMap,
            StringBuilder conditionalQueryStatementsBuilder,
            Map.Entry<String, List<String>> nhsDataArgumentsMap
    ) {
        List<String> admissionSourceList = Optional.of(nhsDataArgumentsMap).map(Map.Entry::getValue).orElse(Collections.emptyList());
        long admissionSourceListCount = admissionSourceList.stream().filter(Objects::nonNull).count();
        if (admissionSourceListCount > 0) {
            conditionalQueryStatementsBuilder.append(FUNCTION_OPEN_BRACES);

            for (int i = 0; i < admissionSourceListCount; i++) {
                conditionalQueryStatementsBuilder
                        .append(ADMISSION_SOURCE_FIELD)
                        .append(EQUALITY_DELIMITER)
                        .append(PREPARED_STATEMENT_DELIMITER);
                if (i < admissionSourceListCount - 1) {
                    conditionalQueryStatementsBuilder.append(OR_STATEMENT);
                }
            }
            conditionalQueryStatementsBuilder.append(FUNCTION_CLOSE_BRACES)
                    .append(AND_STATEMENT)
            ;
        }
        preparedStatementsParameterMap.put(QUERY_PARAM_ADMISSION_SOURCE, admissionSourceList);
    }

    /**
     * Method appending AdmissionEndDate conditional statement builder based on its occurrence in nhsDataArgumentsMap
     * Also populating prepare statements using preparedStatementsParameterMap
     *
     * @param preparedStatementsParameterMap    :   preparedStatementsParameterMap
     * @param conditionalQueryStatementsBuilder :   conditionalQueryStatements String Builder
     * @param nhsDataArgumentsMap               :   Graphql schema arguments map
     */
    private void generateConditionalStatementForAdmissionEndDateList(
            Map<String, Object> preparedStatementsParameterMap,
            StringBuilder conditionalQueryStatementsBuilder,
            Map.Entry<String, List<String>> nhsDataArgumentsMap
    ) {
        List<String> admissionEndDateList = Optional.of(nhsDataArgumentsMap).map(Map.Entry::getValue).orElse(Collections.emptyList());

        String admissionEndDateRangeStart = admissionEndDateList.stream().findFirst().orElse(null);
        String admissionEndDateRangeEnd = admissionEndDateList.stream().skip(1).findFirst().orElse(null);
        conditionalQueryStatementsBuilder
                .append(ADMISSION_END_DATE_FIELD)
                .append(BETWEEN_STATEMENT)
                .append(PREPARED_STATEMENT_DELIMITER)
                .append(AND_STATEMENT)
                .append(PREPARED_STATEMENT_DELIMITER)
                .append(AND_STATEMENT)
        ;
        LocalDate defaultStartDate = LocalDate.of(1000, 1, 1);
        if (isNull(admissionEndDateRangeStart) || isBlank(admissionEndDateRangeStart)) {
            preparedStatementsParameterMap.put(QUERY_PARAM_ADMISSION_END_DATE_RANGE_START, defaultStartDate);
            preparedStatementsParameterMap.put(QUERY_PARAM_ADMISSION_END_DATE_RANGE_END, LocalDate.now());
        } else if (isNull(admissionEndDateRangeEnd) || isBlank(admissionEndDateRangeEnd)) {
            preparedStatementsParameterMap.put(QUERY_PARAM_ADMISSION_END_DATE_RANGE_START,
                    LocalDate.parse(admissionEndDateRangeStart));
            preparedStatementsParameterMap.put(QUERY_PARAM_ADMISSION_END_DATE_RANGE_END, LocalDate.now());
        } else {
            preparedStatementsParameterMap.put(QUERY_PARAM_ADMISSION_END_DATE_RANGE_START,
                    LocalDate.parse(admissionEndDateRangeStart));
            preparedStatementsParameterMap.put(QUERY_PARAM_ADMISSION_END_DATE_RANGE_END,
                    LocalDate.parse(admissionEndDateRangeEnd));
        }
    }

    /**
     * Method appending AdmissionStartDate conditional statement builder based on its occurrence in nhsDataArgumentsMap
     * Also populating prepare statements using preparedStatementsParameterMap
     *
     * @param preparedStatementsParameterMap    :   preparedStatementsParameterMap
     * @param conditionalQueryStatementsBuilder :   conditionalQueryStatements String Builder
     * @param nhsDataArgumentsMap               :   Graphql schema arguments map
     */
    private void generateConditionalStatementForAdmissionStartDateList(
            Map<String, Object> preparedStatementsParameterMap,
            StringBuilder conditionalQueryStatementsBuilder,
            Map.Entry<String, List<String>> nhsDataArgumentsMap
    ) {
        List<String> admissionStartDateList = Optional.of(nhsDataArgumentsMap).map(Map.Entry::getValue).orElse(Collections.emptyList());

        String admissionStartDateRangeStart = admissionStartDateList.stream().findFirst().orElse(null);
        String admissionStartDateRangeEnd = admissionStartDateList.stream().skip(1).findFirst().orElse(null);
        conditionalQueryStatementsBuilder
                .append(ADMISSION_START_DATE_FIELD)
                .append(BETWEEN_STATEMENT)
                .append(PREPARED_STATEMENT_DELIMITER)
                .append(AND_STATEMENT)
                .append(PREPARED_STATEMENT_DELIMITER)
                .append(AND_STATEMENT)
        ;
        LocalDate defaultStartDate = LocalDate.of(1000, 1, 1);

        if (isNull(admissionStartDateRangeStart) || isBlank(admissionStartDateRangeStart)) {
            preparedStatementsParameterMap.put(QUERY_PARAM_ADMISSION_START_DATE_RANGE_START, defaultStartDate);
            preparedStatementsParameterMap.put(QUERY_PARAM_ADMISSION_START_DATE_RANGE_END, LocalDate.now());
        } else if (isNull(admissionStartDateRangeEnd) || isBlank(admissionStartDateRangeEnd)) {
            preparedStatementsParameterMap.put(QUERY_PARAM_ADMISSION_START_DATE_RANGE_START,
                    LocalDate.parse(admissionStartDateRangeStart));
            preparedStatementsParameterMap.put(QUERY_PARAM_ADMISSION_START_DATE_RANGE_END, LocalDate.now());
        } else {
            preparedStatementsParameterMap.put(QUERY_PARAM_ADMISSION_START_DATE_RANGE_START,
                    LocalDate.parse(admissionStartDateRangeStart));
            preparedStatementsParameterMap.put(QUERY_PARAM_ADMISSION_START_DATE_RANGE_END,
                    LocalDate.parse(admissionStartDateRangeEnd));
        }
    }

    /**
     * Method appending Ethnicity conditional statement builder based on its occurrence in nhsDataArgumentsMap
     * Also populating prepare statements using preparedStatementsParameterMap
     *
     * @param preparedStatementsParameterMap    :   preparedStatementsParameterMap
     * @param conditionalQueryStatementsBuilder :   conditionalQueryStatements String Builder
     * @param nhsDataArgumentsMap               :   Graphql schema arguments map
     */
    private void generateConditionalStatementForEthnicityList(
            Map<String, Object> preparedStatementsParameterMap,
            StringBuilder conditionalQueryStatementsBuilder,
            Map.Entry<String, List<String>> nhsDataArgumentsMap
    ) {
        List<String> ethnicityList = Optional.of(nhsDataArgumentsMap).map(Map.Entry::getValue).orElse(Collections.emptyList());
        long ethnicityListCount = ethnicityList.stream().filter(Objects::nonNull).count();
        if (ethnicityListCount > 0) {
            conditionalQueryStatementsBuilder.append(FUNCTION_OPEN_BRACES);

            for (int i = 0; i < ethnicityListCount; i++) {
                conditionalQueryStatementsBuilder
                        .append(PATIENT_ETHNICITY_FIELD)
                        .append(EQUALITY_DELIMITER)
                        .append(PREPARED_STATEMENT_DELIMITER);
                if (i < ethnicityListCount - 1) {
                    conditionalQueryStatementsBuilder.append(OR_STATEMENT);
                }
            }
            conditionalQueryStatementsBuilder.append(FUNCTION_CLOSE_BRACES)
                    .append(AND_STATEMENT);
        }
        preparedStatementsParameterMap.put(QUERY_PARAM_ETHNICITY, ethnicityList);
    }

    /**
     * Method appending SexAtBirth conditional statement builder based on its occurrence in nhsDataArgumentsMap
     * Also populating prepare statements using preparedStatementsParameterMap
     *
     * @param preparedStatementsParameterMap    :   preparedStatementsParameterMap
     * @param conditionalQueryStatementsBuilder :   conditionalQueryStatements String Builder
     * @param nhsDataArgumentsMap               :   Graphql schema arguments map
     */
    private void generateConditionalStatementForSexAtBirthList(
            Map<String, Object> preparedStatementsParameterMap,
            StringBuilder conditionalQueryStatementsBuilder,
            Map.Entry<String, List<String>> nhsDataArgumentsMap
    ) {
        List<String> sexAtBirthList = Optional.of(nhsDataArgumentsMap).map(Map.Entry::getValue).orElse(Collections.emptyList());
        long sexAtBirthListCount = sexAtBirthList.stream().filter(Objects::nonNull).count();
        if (sexAtBirthListCount > 0) {
            conditionalQueryStatementsBuilder.append(FUNCTION_OPEN_BRACES);

            for (int i = 0; i < sexAtBirthListCount; i++) {
                conditionalQueryStatementsBuilder
                        .append(PATIENT_SEX_AT_BIRTH_FIELD)
                        .append(EQUALITY_DELIMITER)
                        .append(PREPARED_STATEMENT_DELIMITER);
                if (i < sexAtBirthListCount - 1) {
                    conditionalQueryStatementsBuilder.append(OR_STATEMENT);
                }
            }
            conditionalQueryStatementsBuilder.append(FUNCTION_CLOSE_BRACES)
                    .append(AND_STATEMENT);
        }
        preparedStatementsParameterMap.put(QUERY_PARAM_SEX_AT_BIRTH, sexAtBirthList);
    }

    /**
     * Method appending YearOfBirth conditional statement builder based on its occurrence in nhsDataArgumentsMap
     * Also populating prepare statements using preparedStatementsParameterMap
     *
     * @param preparedStatementsParameterMap    :   preparedStatementsParameterMap
     * @param conditionalQueryStatementsBuilder :   conditionalQueryStatements String Builder
     * @param nhsDataArgumentsMap               :   Graphql schema arguments map
     */
    private void generateConditionalStatementForYearOfBirthList(
            Map<String, Object> preparedStatementsParameterMap,
            StringBuilder conditionalQueryStatementsBuilder,
            Map.Entry<String, List<String>> nhsDataArgumentsMap
    ) {
        List<String> yearOfBirthList = Optional.of(nhsDataArgumentsMap).map(Map.Entry::getValue).orElse(Collections.emptyList());

        String startYearOfBirth = yearOfBirthList.stream().findFirst().orElse(null);
        String endYearOfBirth = yearOfBirthList.stream().skip(1).findFirst().orElse(null);
        conditionalQueryStatementsBuilder
                .append(PATIENT_YEAR_OF_BIRTH_FIELD)
                .append(BETWEEN_STATEMENT)
                .append(PREPARED_STATEMENT_DELIMITER)
                .append(AND_STATEMENT)
                .append(PREPARED_STATEMENT_DELIMITER)
                .append(AND_STATEMENT)
        ;
        LocalDate defaultStartDate = LocalDate.of(1000, 1, 1);

        if (isNull(startYearOfBirth) || isBlank(startYearOfBirth)) {
            preparedStatementsParameterMap.put(QUERY_PARAM_YEAR_OF_BIRTH_START, defaultStartDate.getYear());
            preparedStatementsParameterMap.put(QUERY_PARAM_YEAR_OF_BIRTH_END, LocalDate.now().getYear());
        } else if (isNull(endYearOfBirth) || isBlank(endYearOfBirth)) {
            preparedStatementsParameterMap.put(QUERY_PARAM_YEAR_OF_BIRTH_START,
                    LocalDate.of(Integer.parseInt(startYearOfBirth), 1, 1).getYear());
            preparedStatementsParameterMap.put(QUERY_PARAM_YEAR_OF_BIRTH_END, LocalDate.now().getYear());
        } else {
            preparedStatementsParameterMap.put(QUERY_PARAM_YEAR_OF_BIRTH_START,
                    LocalDate.of(Integer.parseInt(startYearOfBirth), 1, 1).getYear());
            preparedStatementsParameterMap.put(QUERY_PARAM_YEAR_OF_BIRTH_END,
                    LocalDate.of(Integer.parseInt(endYearOfBirth), 12, 31).getYear());
        }
    }

    /**
     * Method appending PatientId conditional statement builder based on its occurrence in nhsDataArgumentsMap
     * Also populating prepare statements using preparedStatementsParameterMap
     *
     * @param preparedStatementsParameterMap    :   preparedStatementsParameterMap
     * @param conditionalQueryStatementsBuilder :   conditionalQueryStatements String Builder
     * @param nhsDataArgumentsMap               :   Graphql schema arguments map
     */
    private void generateConditionalStatementForPatientIdList(
            Map<String, Object> preparedStatementsParameterMap,
            StringBuilder conditionalQueryStatementsBuilder,
            Map.Entry<String, List<String>> nhsDataArgumentsMap
    ) {
        List<String> patientIdList = Optional.of(nhsDataArgumentsMap).map(Map.Entry::getValue).orElse(Collections.emptyList());
        String patientId = patientIdList.stream().findFirst().orElse(null);
        if (nonNull(patientId)) {
            conditionalQueryStatementsBuilder
                    .append(PATIENT_ID_FIELD)
                    .append(EQUALITY_DELIMITER)
                    .append(PREPARED_STATEMENT_DELIMITER)
                    .append(AND_STATEMENT)
            ;
            preparedStatementsParameterMap.put(QUERY_PARAM_PATIENT_ID,
                    patientId);
        } else {
            conditionalQueryStatementsBuilder
                    .append(PATIENT_ID_FIELD)
                    .append(LIKE_STATEMENT)
                    .append(PREPARED_STATEMENT_DELIMITER)
                    .append(AND_STATEMENT)
            ;
            preparedStatementsParameterMap.put(QUERY_PARAM_PATIENT_ID, MATCHING_DELIMITER);
        }
    }

    /**
     * Method constructing fields for dynamic query by appending required table field values
     *
     * @return {@link String}
     */
    public static String getDisplayFieldsForQuery() {
        return PATIENT_ID_FIELD + FIELD_DELIMITER +
                PATIENT_YEAR_OF_BIRTH_FIELD + FIELD_DELIMITER +
                PATIENT_SEX_AT_BIRTH_FIELD + FIELD_DELIMITER +
                PATIENT_ETHNICITY_FIELD + FIELD_DELIMITER +
                ADMISSION_START_DATE_FIELD + FIELD_DELIMITER +
                ADMISSION_END_DATE_FIELD + FIELD_DELIMITER +
                ADMISSION_SOURCE_FIELD + FIELD_DELIMITER +
                ADMISSION_OUTCOME_FIELD;
    }

    /**
     * Method populating entityManagerQuery with prepare statements from preparedStatementsParameterMap
     * based on their occurrences
     *
     * @param preparedStatementsParameterMap :   prepare statement parameter map
     * @param entityManagerQuery             :   EntityManagerQuery Object
     */
    public void setPreparedStatementsParameters(
            Map<String, Object> preparedStatementsParameterMap,
            Query entityManagerQuery
    ) {
        final int[] counter = {0};
        preparedStatementsParameterMap.entrySet().forEach(entry -> {
            String key = Optional.ofNullable(entry).map(Map.Entry::getKey).orElse("");
            Object value = Optional.ofNullable(entry).map(Map.Entry::getValue).orElse(null);

            if (nonNull(value)) {
                if (key.equals(QUERY_PARAM_PATIENT_ID)) {
                    entityManagerQuery.setParameter(1, value);
                } else if (key.equals(QUERY_PARAM_YEAR_OF_BIRTH_START)) {
                    entityManagerQuery.setParameter(2, value);
                } else if (key.equals(QUERY_PARAM_YEAR_OF_BIRTH_END)) {
                    entityManagerQuery.setParameter(3, value);
                } else if (key.equals(QUERY_PARAM_SEX_AT_BIRTH)) {
                    List<String> sexATBirthList = (List<String>) value;
                    counter[0] = 4;
                    for (int i = 4, j = 0; j < sexATBirthList.size(); i++, j++) {
                        entityManagerQuery.setParameter(i, sexATBirthList.get(j));
                        counter[0] = i + 1;
                    }
                } else if (key.equals(QUERY_PARAM_ETHNICITY)) {
                    List<String> ethnicityList = (List<String>) value;
                    for (int i = counter[0], j = 0; j < ethnicityList.size(); i++, j++) {
                        entityManagerQuery.setParameter(i, ethnicityList.get(j));
                        counter[0] = i + 1;
                    }
                } else if (key.equals(QUERY_PARAM_ADMISSION_START_DATE_RANGE_START)) {
                    entityManagerQuery.setParameter(counter[0], value);
                    counter[0] = counter[0] + 1;
                } else if (key.equals(QUERY_PARAM_ADMISSION_START_DATE_RANGE_END)) {
                    entityManagerQuery.setParameter(counter[0], value);
                    counter[0] = counter[0] + 1;
                } else if (key.equals(QUERY_PARAM_ADMISSION_END_DATE_RANGE_START)) {
                    entityManagerQuery.setParameter(counter[0], value);
                    counter[0] = counter[0] + 1;
                } else if (key.equals(QUERY_PARAM_ADMISSION_END_DATE_RANGE_END)) {
                    entityManagerQuery.setParameter(counter[0], value);
                    counter[0] = counter[0] + 1;
                } else if (key.equals(QUERY_PARAM_ADMISSION_SOURCE)) {
                    List<String> admissionSourceList = (List<String>) value;
                    for (int i = counter[0], j = 0; j < admissionSourceList.size(); i++, j++) {
                        entityManagerQuery.setParameter(i, admissionSourceList.get(j));
                        counter[0] = i + 1;
                    }
                } else if (key.equals(QUERY_PARAM_ADMISSION_OUTCOME)) {
                    List<String> admissionOutComeList = (List<String>) value;
                    for (int i = counter[0], j = 0; j < admissionOutComeList.size(); i++, j++) {
                        entityManagerQuery.setParameter(i, admissionOutComeList.get(j));
                        counter[0] = i + 1;
                    }
                }
            }
        });

    }
}

package com.national.health.service.repository;

import com.national.health.service.converter.DBDomainConverter;
import com.national.health.service.model.DBAdmission;
import com.national.health.service.model.DBPatient;
import com.national.health.service.model.DomainAdmission;
import com.national.health.service.model.DomainPatient;
import com.national.health.service.model.NHSData;
import com.national.health.service.util.NHSDirectoryServiceInfraUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import static com.national.health.service.constants.InfraConstants.GENERIC_INFRA_ERROR_MESSAGE;
import static com.national.health.service.constants.InfraConstants.JOIN_EQUALITY_STATEMENT;
import static com.national.health.service.constants.InfraConstants.JOIN_STATEMENT;
import static com.national.health.service.constants.InfraConstants.LIMIT_STATEMENT;
import static com.national.health.service.constants.InfraConstants.OFFSET_STATEMENT;
import static com.national.health.service.constants.InfraConstants.SELECT_STATEMENT;
import static com.national.health.service.util.NHSDirectoryServiceInfraUtility.getDisplayFieldsForQuery;

/**
 * Repository class performing repository operations
 */
@Repository
public class MysqlRepositoryImpl implements MysqlRepository {

    private static final Logger logger = LoggerFactory.getLogger(MysqlRepositoryImpl.class);

    @Value("${data.rows.limit}")
    private Long dataRowsLimit;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AdmissionRepository admissionRepository;

    @Autowired
    private DBDomainConverter dbDomainConverter;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private NHSDirectoryServiceInfraUtility nhsDirectoryServiceInfraUtility;

    /**
     * Method saves List of Domain Patient to DB using {@link PatientRepository}
     *
     * @param domainPatientList : List od Domain Patients
     */
    @Override
    public void savePatientListData(List<DomainPatient> domainPatientList) {
        List<DBPatient> dbPatientList = dbDomainConverter.domainPatientToDBPatient(domainPatientList);
        patientRepository.saveAll(dbPatientList);
    }

    /**
     * Method saves List of Domain Admission to DB using {@link AdmissionRepository}
     *
     * @param domainAdmissionList :   : List od Domain Admissions
     */
    @Override
    public void saveAdmissionListData(List<DomainAdmission> domainAdmissionList) {
        List<DBAdmission> dbAdmissionList = dbDomainConverter.domainAdmissionToDBAdmission(domainAdmissionList);
        admissionRepository.saveAll(dbAdmissionList);
    }

    /**
     * Method Querying Patient and Admission Table based on Graphql schema arguments to find matching NHSData List
     *
     * @param nhsDataArgumentsMap :   Map of Graphql schema arguments
     * @param offsetArgument      :   Query offset argument
     * @return {@link List<NHSData>}
     */
    @Override
    public List<NHSData> findNHSData(Map<String, List<String>> nhsDataArgumentsMap, Long offsetArgument) {
        Map<String, Object> preparedStatementsParameterMap = new LinkedHashMap<>();
        String query = generateQueryFromNHSDataArgumentsMap(nhsDataArgumentsMap, preparedStatementsParameterMap, offsetArgument);
        Query entityManagerQuery = entityManager.createNativeQuery(query);
        nhsDirectoryServiceInfraUtility.setPreparedStatementsParameters(preparedStatementsParameterMap, entityManagerQuery);
        List<NHSData> nhsDataList = new ArrayList<>();
        try {
            List<Object[]> resultList = (List<Object[]>) entityManagerQuery.getResultList();
            resultList.stream().filter(Objects::nonNull).forEach(populateNHSDataList(nhsDataList));
        } catch (Exception exception) {
            logger.error(GENERIC_INFRA_ERROR_MESSAGE, exception);
        }
        return nhsDataList;
    }

    /**
     * Method populating NHSData List from Query results
     *
     * @param nhsDataList : List of NHSData
     * @return {@link Consumer}
     */
    private Consumer populateNHSDataList(List<NHSData> nhsDataList) {
        return resultObject -> {
            Optional.ofNullable(resultObject).ifPresent(object -> {
                NHSData nhsData = new NHSData();
                Object[] objectArray = (Object[]) object;
                try {
                    Optional.ofNullable(objectArray[0]).ifPresent(objectArrayValue -> nhsData.setPatientId(objectArrayValue.toString()));
                    Optional.ofNullable(objectArray[1]).ifPresent(objectArrayValue -> nhsData.setYearOfBirth(Integer.parseInt(objectArrayValue.toString())));
                    Optional.ofNullable(objectArray[2]).ifPresent(objectArrayValue -> nhsData.setSexAtBirth(objectArrayValue.toString()));
                    Optional.ofNullable(objectArray[3]).ifPresent(objectArrayValue -> nhsData.setEthnicity(objectArrayValue.toString()));
                    Optional.ofNullable(objectArray[4]).ifPresent(objectArrayValue -> nhsData.setAdmissionStartDateTime(objectArrayValue.toString()));
                    Optional.ofNullable(objectArray[5]).ifPresent(objectArrayValue -> nhsData.setAdmissionEndDateTime(objectArrayValue.toString()));
                    Optional.ofNullable(objectArray[6]).ifPresent(objectArrayValue -> nhsData.setAdmissionSource(objectArrayValue.toString()));
                    Optional.ofNullable(objectArray[7]).ifPresent(objectArrayValue -> nhsData.setAdmissionOutcome(objectArrayValue.toString()));
                } catch (ArrayIndexOutOfBoundsException exception) {
                    logger.error(GENERIC_INFRA_ERROR_MESSAGE, exception);
                }
                nhsDataList.add(nhsData);
            });
        };
    }

    /**
     * Method generating Query statement
     *
     * @param nhsDataArgumentsMap            :   Graphql schema Arguments map
     * @param preparedStatementsParameterMap :   Prepare statement parameter map
     * @param offsetArgument                 :   Offset Argument
     * @return {@link String}
     */
    private String generateQueryFromNHSDataArgumentsMap(
            Map<String, List<String>> nhsDataArgumentsMap,
            Map<String, Object> preparedStatementsParameterMap,
            Long offsetArgument) {
        return SELECT_STATEMENT + getDisplayFieldsForQuery() + JOIN_STATEMENT + JOIN_EQUALITY_STATEMENT +
                nhsDirectoryServiceInfraUtility.getConditionalQueryStatements(
                        nhsDataArgumentsMap, preparedStatementsParameterMap) +
                LIMIT_STATEMENT + dataRowsLimit + OFFSET_STATEMENT + offsetArgument;
    }


}

package com.national.health.service.controller;

import com.national.health.service.converter.RestDomainConverter;
import com.national.health.service.model.RestAdmission;
import com.national.health.service.model.RestPatient;
import com.national.health.service.processor.DataProcessor;
import com.national.health.service.service.GraphQLService;
import com.national.health.service.service.GraphQLServiceImpl;
import graphql.ExecutionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * API Controller for handling REST Requests
 */
@RequestMapping("/api")
@RestController
public class APIController {

    private static final Logger logger = LoggerFactory.getLogger(APIController.class);

    @Autowired
    private DataProcessor dataProcessor;

    @Autowired
    private GraphQLService graphQLService;

    @Autowired
    private RestDomainConverter restDomainConverter;

    /**
     * Method to save Patient data into DB
     * @param restPatientList   : restPatient List
     */
    @PostMapping("/savePatients")
    public void savePatient(@RequestBody List<RestPatient> restPatientList){
        logger.info("Reached APIController Controller savePatients");
        dataProcessor.patientDataProcessor(restDomainConverter.restPatientToDomainPatient(restPatientList));
    }

    /**
     * Method to save Admission data into DB
     * @param restAdmissionList     :   restAdmission List
     */
    @PostMapping("/saveAdmissions")
    public void saveAdmission(@RequestBody List<RestAdmission> restAdmissionList){
        logger.info("Reached APIController Controller saveAdmissions");
        dataProcessor.admissionDataProcessor(restDomainConverter.restAdmissionToDomainAdmission(restAdmissionList));
    }

    /**
     * Graphql Query end point to fetch query data
     * @param query     :   Graphql schema
     * @return {@link ResponseEntity<Object>}
     */
    @PostMapping("/query-nhs")
    public ResponseEntity<Object> getNHSQueryResults(@RequestBody String query){
        ExecutionResult execute = graphQLService.getGraphQL().execute(query);
        return new ResponseEntity<>(execute, HttpStatus.OK);
    }

}

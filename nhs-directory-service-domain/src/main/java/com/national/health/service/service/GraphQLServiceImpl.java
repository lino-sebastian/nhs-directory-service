package com.national.health.service.service;

import com.national.health.service.datafetcher.NHSDataFetcher;
import com.national.health.service.repository.MysqlRepository;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import static com.national.health.service.constants.DomainConstants.GENERIC_EXCEPTION_ERROR_MESSAGE;
import static com.national.health.service.constants.DomainConstants.GRAPHQL_SCHEMA;
import static com.national.health.service.constants.DomainConstants.GRAPHQL_SCHEMA_FIELD_NAME;
import static com.national.health.service.constants.DomainConstants.GRAPHQL_SCHEMA_LOADING_MESSAGE;

/**
 * GraphQLServiceImpl class implementing GraphQLService
 * for handling schema loading, runtime wiring and generate GraphQL
 */
@Service
public class GraphQLServiceImpl implements GraphQLService {

    private static final Logger logger = LoggerFactory.getLogger(GraphQLServiceImpl.class);

    @Autowired
    private NHSDataFetcher nhsDataFetcher;

    @Autowired
    private MysqlRepository mysqlRepository;

    private GraphQL graphQL;

    /**
     * Populating Graphql schema after bean initialization
     *
     * @throws IOException
     */
    @PostConstruct
    private void loadSchema() throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream("/data.graphql");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String contents = reader.lines()
                    .collect(Collectors.joining(System.lineSeparator()));

            TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(contents);
            RuntimeWiring wiring = buildRuntimeWiring();
            GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
            logger.info(GRAPHQL_SCHEMA_LOADING_MESSAGE);
            graphQL = GraphQL.newGraphQL(schema).build();
        } catch (Exception exception) {
            logger.error(GENERIC_EXCEPTION_ERROR_MESSAGE, exception);
        }

    }

    /**
     * Provides global GraphQL
     *
     * @return {@link GraphQL}
     */
    @Override
    public GraphQL getGraphQL() {
        return graphQL;
    }

    /**
     * Method performing runtime wiring for Graphql based on the
     * graphql schema and data fetcher provided
     *
     * @return {@link RuntimeWiring}
     */
    private RuntimeWiring buildRuntimeWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type(GRAPHQL_SCHEMA, typeWiring -> typeWiring
                        .dataFetcher(GRAPHQL_SCHEMA_FIELD_NAME, nhsDataFetcher))
                .build();
    }
}

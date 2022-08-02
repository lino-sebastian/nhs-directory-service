package com.national.health.service.service;

import graphql.GraphQL;

/**
 * Interface for handling Graphql Service from Infra Layer
 */
public interface GraphQLService {
    /**
     * Method providing global Graphql
     *
     * @return {@link GraphQL}
     */
    GraphQL getGraphQL();
}

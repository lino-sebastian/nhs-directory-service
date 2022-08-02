package com.national.health.service.config;

import com.national.health.service.aggregator.DataAggregator;
import com.national.health.service.converter.RestDomainConverter;
import com.national.health.service.processor.DataProcessor;
import com.national.health.service.processor.DataProcessorImpl;
import com.national.health.service.route.DataReaderRoute;
import org.apache.camel.CamelContext;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring boot Configuration class configuring user defined beans for application
 */
@Configuration
public class ApplicationConfiguration {

    private static final String PATH_PATTERNS = "/**";
    private static final String HTTP_GET_METHOD = "GET";
    private static final String HTTP_POST_METHOD = "POST";
    private static final String WILD_CARD_PARAM = "*";

    @Autowired
    private CamelContext camelContext;

    /**
     * Method configuring bean for Camel DataReader Route and registering the bean into {@link CamelContext}
     *
     * @return {@link DataReaderRoute}
     * @throws Exception
     */
    @Bean
    public DataReaderRoute dataReaderRoute() throws Exception {
        DataReaderRoute dataReaderRoute = new DataReaderRoute();
        camelContext.addRoutes(dataReaderRoute);
        return dataReaderRoute;
    }

    /**
     * Method configuring bean for Camel DataAggregator bean
     *
     * @return {@link DataAggregator}
     */
    @Bean
    public DataAggregator dataAggregator() {
        return new DataAggregator();
    }

    /**
     * Method configuring bean for RestDomain converter, converting Rest Domain model
     *
     * @return {@link RestDomainConverter}
     */
    @Bean
    public RestDomainConverter restToDomainConverter() {
        return new RestDomainConverter();
    }

    /**
     * Method configuring bean for DataProcessor processing data between Rest(Infra) Domain Layers
     *
     * @return {@link DataProcessor}
     */
    @Bean
    public DataProcessor dataProcessor() {
        return new DataProcessorImpl();
    }

    /**
     * Method configuring WebMvcConfigurations including CORS Mappings
     *
     * @return {@link WebMvcConfigurer}
     */
    @Bean
    public WebMvcConfigurer configure() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NotNull CorsRegistry corsRegistry) {
                corsRegistry.addMapping(PATH_PATTERNS)
                        .allowedMethods(HTTP_GET_METHOD, HTTP_POST_METHOD)
                        .allowedHeaders(WILD_CARD_PARAM)
                        .allowedOrigins(WILD_CARD_PARAM)

                ;
            }
        };
    }
}

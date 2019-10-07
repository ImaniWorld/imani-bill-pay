package com.imani.bill.pay.service.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author manyce400
 */
@Configuration
public class RestTemplateConfigurator {

    public static final String SERVICE_REST_TEMPLATE = "com.imani.bill.pay.service.rest.RestTemplateConfigurator";

    @Bean(RestTemplateConfigurator.SERVICE_REST_TEMPLATE)
    public RestTemplate configureServiceRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }
}

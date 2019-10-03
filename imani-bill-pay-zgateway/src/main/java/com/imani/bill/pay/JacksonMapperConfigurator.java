package com.imani.bill.pay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author manyce400
 */
@Configuration
public class JacksonMapperConfigurator {


    public static final String JACSKON_OBJECT_MAPPER = "com.imani.bill.pay.JacksonMapperConfigurator";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(JacksonMapperConfigurator.class);

    @Bean(JacksonMapperConfigurator.JACSKON_OBJECT_MAPPER)
    public ObjectMapper configureObjectMapper() {
        LOGGER.info("Configuring and tuning Imani BillPay Jackson ObjectMapper capabilities.....");
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Jdk8Module());
        mapper.registerModule(new JodaModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

}

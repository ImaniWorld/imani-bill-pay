package com.imani.bill.pay.domain.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;

/**
 * @author manyce400
 */
public class MockObjectMapper extends ObjectMapper {


    public MockObjectMapper() {
        registerModule(new Jdk8Module());
        registerModule(new JodaModule());
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

}

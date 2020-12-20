package com.imani.bill.pay.service.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

/**
 * @author manyce400
 */
public interface IRestUtil {

    public HttpHeaders getRestJSONHeader();

    public HttpEntity<String> getObjectAsRequest(Object o) throws JsonProcessingException;
}

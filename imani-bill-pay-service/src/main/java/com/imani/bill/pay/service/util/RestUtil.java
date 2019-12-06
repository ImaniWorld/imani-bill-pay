package com.imani.bill.pay.service.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service(RestUtil.SPRING_BEAN)
public class RestUtil implements IRestUtil {



    public static final String SPRING_BEAN = "com.imani.bill.pay.service.util.RestUtil";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(RestUtil.class);

    @Override
    public HttpHeaders getRestJSONHeader() {
        LOGGER.debug("Building new HttpHeaders for JSON request....");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

}

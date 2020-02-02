package com.imani.bill.pay.service.payment.plaid;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imani.bill.pay.domain.payment.plaid.*;
import com.imani.bill.pay.service.rest.RestTemplateConfigurator;
import com.imani.bill.pay.service.util.IRestUtil;
import com.imani.bill.pay.service.util.RestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

/**
 * @author manyce400
 */
@Service(PlaidAPIEndPointFacade.SPRING_BEAN)
public class PlaidAPIEndPointFacade implements IPlaidAPIEndPointFacade {


    @Autowired
    @Qualifier(RestUtil.SPRING_BEAN)
    private IRestUtil iRestUtil;

    @Autowired
    @Qualifier(RestTemplateConfigurator.SERVICE_REST_TEMPLATE)
    private RestTemplate restTemplate;

    @Autowired
    @Qualifier(PlaidAPIInvocationStatisticService.SPRING_BEAN)
    private IPlaidAPIInvocationStatisticService iPlaidAPIInvocationStatisticService;

    @Autowired
    private ObjectMapper mapper;


    public static final String SPRING_BEAN = "com.imani.bill.pay.service.payment.plaid.PlaidAPIEndPointFacade";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PlaidAPIEndPointFacade.class);

    @Override
    public <O extends PlaidAPIResponse> O invokePlaidAPIEndPoint(PlaidAPIInvocationStatistic plaidAPIInvocationStatistic, String apiURL, O responseObj) {
        Assert.notNull(plaidAPIInvocationStatistic, "PlaidAPIInvocationStatistic cannot be null");
        Assert.notNull(apiURL, "apiURL cannot be null");
        Assert.isTrue(apiURL.length() > 0, "apiURL cannot be empty");
        Assert.notNull(responseObj, "responseObj cannot be null");

        LOGGER.info("Invoking Plaid API EndPoint with invocation mode:=> {} and URL:=> {}", plaidAPIInvocationStatistic.getPlaidAPIInvocationE(), apiURL);

        // Build Plaid API request body for calling end point
        Optional<HttpEntity<String>> request = getAPIEntityRequest(plaidAPIInvocationStatistic.getPlaidAPIRequest());

        if(request.isPresent()) {
            plaidAPIInvocationStatistic.startAPIInvocation();

            try {
                if (responseObj instanceof PlaidAccessTokenResponse) {
                    PlaidAccessTokenResponse plaidAccessTokenResponse = restTemplate.postForObject(apiURL, request.get(), PlaidAccessTokenResponse.class);
                    ((PlaidAccessTokenResponse) responseObj).setAccessToken(plaidAccessTokenResponse.getAccessToken());
                    plaidAPIInvocationStatistic.setPlaidAPIResponse(plaidAccessTokenResponse);
                } else if(responseObj instanceof PlaidItemAccountsResponse) {
                    PlaidItemAccountsResponse plaidItemAccountsResponse = restTemplate.postForObject(apiURL, request.get(), PlaidItemAccountsResponse.class);
                    System.out.println("plaidItemAccountsResponse = " + plaidItemAccountsResponse);
                    ((PlaidItemAccountsResponse) responseObj).setPlaidItemInfo(plaidItemAccountsResponse.getPlaidItemInfo());
                    ((PlaidItemAccountsResponse) responseObj).setAccounts(plaidItemAccountsResponse.getAccounts());
                } else if(responseObj instanceof StripeBankAccountResponse) {
                    StripeBankAccountResponse stripeBankAccountResponse = restTemplate.postForObject(apiURL, request.get(), StripeBankAccountResponse.class);
                    System.out.println("stripeBankAccountResponse = " + stripeBankAccountResponse);
                    ((StripeBankAccountResponse) responseObj).setStripeBankAcctToken(stripeBankAccountResponse.getStripeBankAcctToken());
                }
            } catch (HttpClientErrorException e) {
                buildResponseWithAPIException(e, responseObj);
            }
        }

        // End invocation and update statistics with the response that we received from Plaid API EndPoint
        plaidAPIInvocationStatistic.endAPIInvocation();
        plaidAPIInvocationStatistic.setPlaidAPIResponse(responseObj);
        iPlaidAPIInvocationStatisticService.save(plaidAPIInvocationStatistic);
        return responseObj;
    }


    Optional<HttpEntity<String>> getAPIEntityRequest(PlaidAPIRequest plaidAPIRequest) {
        LOGGER.debug("Building HTTP request body for Plaid API endpoint using plaidAPIRequest:=> {}", plaidAPIRequest);
        HttpEntity<String> requestHttpEntity = null;
        try {
            HttpHeaders httpHeaders = iRestUtil.getRestJSONHeader();
            String request = mapper.writeValueAsString(plaidAPIRequest);
            requestHttpEntity = new HttpEntity<>(request, httpHeaders);
            return Optional.of(requestHttpEntity);
        } catch (JsonProcessingException e) {
            LOGGER.warn("Failed to construct request body for Plaid API Endpoint", e);
        }

        return Optional.empty();
    }

    <O extends PlaidAPIResponse> void buildResponseWithAPIException(Exception exception, O responseObj) {
        if(exception instanceof JsonProcessingException) {
            // In this case processing JSON response has failed
            LOGGER.warn("Exception occurred while processing JSON response from Plaid API", exception);
            responseObj.setDisplayMessage("JSON processing exception occurred");
        } else if(exception instanceof HttpClientErrorException) {
            // Client exception on calling API has occurred, could be some bad request that we sent
            LOGGER.warn("API Client Exception occurred.  Bad request detected");
            LOGGER.info("============================  Start Plaid API Response ============================");
            String responseBody = ((HttpClientErrorException)exception).getResponseBodyAsString();
            try {
                PlaidAPIResponse plaidAPIResponse = mapper.readValue(responseBody, PlaidAPIResponse.class);
                LOGGER.info("{}", plaidAPIResponse);
                LOGGER.info("============================  End Plaid API Response ============================");
                responseObj.from(plaidAPIResponse);
            } catch (Exception e) {
                LOGGER.error("Exception occurred while trying to retrieve Plaid API client error", e);
                responseObj.setErrorMessage("*** Received unreadable exception from Plaid API ***");
            }
        } else {
            // All other exceptions add message
            LOGGER.warn("Unexpected Exception occurred while invoking Plaid API", exception);
            responseObj.setErrorMessage("*** Exception occurred while invoking Plaid API. Investigate. ***");
        }
    }
}

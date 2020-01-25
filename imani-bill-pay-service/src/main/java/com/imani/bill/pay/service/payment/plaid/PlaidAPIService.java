package com.imani.bill.pay.service.payment.plaid;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imani.bill.pay.domain.payment.config.PlaidAPIConfig;
import com.imani.bill.pay.domain.payment.plaid.*;
import com.imani.bill.pay.domain.user.UserRecord;
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
@Service(PlaidAPIService.SPRING_BEAN)
public class PlaidAPIService implements IPlaidAPIService {


    @Autowired
    private PlaidAPIConfig plaidAPIConfig;

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


    public static final String ITEM_ACCOUNTS_RETRIEVE_PATH = "/accounts/get";

    public static final String CREATE_STRIPE_BANK_TOKEN_PATH = "/processor/stripe/bank_account_token/create";


    public static final String SPRING_BEAN = "com.imani.bill.pay.service.payment.plaid.PlaidAPIService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PlaidAPIService.class);


    @Override
    public Optional<StripeBankAccountResponse> createStripeBankAccount(PlaidAPIRequest plaidAPIRequest) {
        Assert.notNull(plaidAPIRequest, "PlaidAPIRequest cannot be null");
        Assert.notNull(plaidAPIRequest.getSecret(), "Plaid secret cannot be null");
        Assert.notNull(plaidAPIRequest.getClientID(), "Plaid clientID cannot be null");
        Assert.notNull(plaidAPIRequest.getAccessToken(), "Plaid accessToken cannot be null");
        Assert.notNull(plaidAPIRequest.getAccountID(), "Plaid accountID cannot be null");

        try {
            HttpHeaders httpHeaders = iRestUtil.getRestJSONHeader();
            String request = mapper.writeValueAsString(plaidAPIRequest);
            HttpEntity<String> requestHttpEntity = new HttpEntity<>(request, httpHeaders);

            // Build API URL for requesting Plaid Access Token and post for response
            String apiURL = plaidAPIConfig.getAPIPathURL(CREATE_STRIPE_BANK_TOKEN_PATH);
            LOGGER.info("Invoking Plaid API to create a Stripe Connected Bank Acct  with URL:=> {}", apiURL);
            StripeBankAccountResponse accessTokenResponse = restTemplate.postForObject(apiURL, requestHttpEntity, StripeBankAccountResponse.class);
            return Optional.of(accessTokenResponse);
        } catch (JsonProcessingException e) {
            LOGGER.warn("Failed to create connected Strip Bank Account for given Plaid Account details.", e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<PlaidItemAccountsResponse> getPlaidItemAccounts(PlaidAPIRequest plaidAPIRequest) {
        Assert.notNull(plaidAPIRequest, "PlaidAPIRequest cannot be null");
        Assert.notNull(plaidAPIRequest.getSecret(), "Plaid secret cannot be null");
        Assert.notNull(plaidAPIRequest.getClientID(), "Plaid clientID cannot be null");
        Assert.notNull(plaidAPIRequest.getAccessToken(), "Plaid accessToken cannot be null");
        Assert.notNull(plaidAPIRequest.getAccountID(), "Plaid accountID cannot be null");

        try {
            HttpHeaders httpHeaders = iRestUtil.getRestJSONHeader();
            String request = mapper.writeValueAsString(plaidAPIRequest);
            HttpEntity<String> requestHttpEntity = new HttpEntity<>(request, httpHeaders);

            // Build API URL for getting all an Items accounts
            String apiURL = plaidAPIConfig.getAPIPathURL(ITEM_ACCOUNTS_RETRIEVE_PATH);
            LOGGER.info("Invoking Plaid API to retrieve Item Accounts with URL:=> {}", apiURL);
            PlaidItemAccountsResponse plaidItemAccountsResponse = restTemplate.postForObject(apiURL, requestHttpEntity, PlaidItemAccountsResponse.class);
            return Optional.of(plaidItemAccountsResponse);
        } catch (JsonProcessingException e) {
            LOGGER.warn("Failed to create connected Strip Bank Account for given Plaid Account details.", e);
        }

        return Optional.empty();
    }


    @Override
    public Optional<PlaidAccessTokenResponse> exchangePublicTokenForAccess(String plaidPublicToken, UserRecord userRecord) {
        Assert.notNull(plaidPublicToken, "plaidPublicToken cannot be null");
        LOGGER.info("Exchanging Plaid Account public token for Access Token....");

        // Build param object to send request for access token
        PlaidAPIRequest plaidAPIRequest = PlaidAPIRequest.builder()
                .secret(plaidAPIConfig.getSecret())
                .clientID(plaidAPIConfig.getClientID())
                .publicToken(plaidPublicToken)
                .build();

        return exchangePublicTokenForAccess(plaidAPIRequest, userRecord);
    }

    /**
     * In order to access any details of an Item(Account, etc) Plaid requires the exchange of a Public token associated with that item for an Access Token.
     * Accessing data about an Item for example account balances, transactions etc requires obtaining an Access Token first.
     *
     * Once an Access Token is retrieved, it can be included in all request for Item details.
     *
     * @param plaidAPIRequest
     * @return
     */
    @Override
    public Optional<PlaidAccessTokenResponse> exchangePublicTokenForAccess(PlaidAPIRequest plaidAPIRequest, UserRecord userRecord) {
        Assert.notNull(plaidAPIRequest, "PlaidAPIRequest cannot be null");
        Assert.notNull(plaidAPIRequest.getSecret(), "Plaid secret cannot be null");
        Assert.notNull(plaidAPIRequest.getClientID(), "Plaid clientID cannot be null");
        Assert.notNull(plaidAPIRequest.getPublicToken(), "Plaid publicToken cannot be null");
        Assert.notNull(plaidAPIRequest.getPublicToken(), "Plaid publicToken cannot be null");

        LOGGER.info("Invoking Plaid API to retrieve an access token using PlaidAPIRequest:=> {}", plaidAPIRequest);

        PlaidAPIInvocationStatistic plaidAPIInvocationStatistic = null;
        PlaidAccessTokenResponse plaidAccessTokenResponse = new PlaidAccessTokenResponse();

        try {
            HttpHeaders httpHeaders = iRestUtil.getRestJSONHeader();
            String request = mapper.writeValueAsString(plaidAPIRequest);
            HttpEntity<String> requestHttpEntity = new HttpEntity<>(request, httpHeaders);

            String apiURL = plaidAPIConfig.getAPIPathURL("/item/public_token/exchange");
            LOGGER.info("Invoking Plaid API to exchange Public Token for Access Token with URL:=> {}", apiURL);

            plaidAPIInvocationStatistic = iPlaidAPIInvocationStatisticService.startPlaidAPIInvocation(userRecord, PlaidAPIInvocationE.AccessToken, plaidAPIRequest);
            plaidAccessTokenResponse = restTemplate.postForObject(apiURL, requestHttpEntity, PlaidAccessTokenResponse.class);
            iPlaidAPIInvocationStatisticService.endPlaidAPIInvocation(plaidAPIInvocationStatistic, plaidAccessTokenResponse);
            return Optional.of(plaidAccessTokenResponse);
        } catch (JsonProcessingException e) {
             enhanceResponseOnException(e, plaidAccessTokenResponse);
            iPlaidAPIInvocationStatisticService.endPlaidAPIInvocation(plaidAPIInvocationStatistic, plaidAccessTokenResponse);
        } catch (HttpClientErrorException e) {
            enhanceResponseOnException(e, plaidAccessTokenResponse);
            iPlaidAPIInvocationStatisticService.endPlaidAPIInvocation(plaidAPIInvocationStatistic, plaidAccessTokenResponse);
        }

        return Optional.of(plaidAccessTokenResponse);
    }

    public <O extends PlaidAPIResponse> void enhanceResponseOnException(Exception exception, O responseObj) {
        if(exception instanceof JsonProcessingException) {
             // In this case either processing JSON request or JSON response has failed
            LOGGER.warn("JSON Processing Exception occurred", exception);
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

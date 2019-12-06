package com.imani.bill.pay.service.payment.plaid;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imani.bill.pay.domain.payment.config.PlaidAPIConfig;
import com.imani.bill.pay.domain.payment.plaid.AccessTokenResponse;
import com.imani.bill.pay.domain.payment.plaid.PlaidAPIRequest;
import com.imani.bill.pay.domain.payment.plaid.StripeBankAccountResponse;
import com.imani.bill.pay.service.rest.RestTemplateConfigurator;
import com.imani.bill.pay.service.util.IRestUtil;
import com.imani.bill.pay.service.util.RestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
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
    private ObjectMapper mapper;


    public static final String CREATE_STRIPE_BANK_TOKEN_PATH = "/processor/stripe/bank_account_token/create";


    public static final String SPRING_BEAN = "com.imani.bill.pay.service.payment.plaid.PlaidAPIService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PlaidAPIService.class);

    @Override
    public Optional<AccessTokenResponse> exchangePublicTokenForAccess(PlaidAPIRequest plaidAPIRequest) {
        Assert.notNull(plaidAPIRequest, "PlaidAPIRequest cannot be null");
        Assert.notNull(plaidAPIRequest.getSecret(), "Plaid secret cannot be null");
        Assert.notNull(plaidAPIRequest.getClientID(), "Plaid clientID cannot be null");
        Assert.notNull(plaidAPIRequest.getPublicToken(), "Plaid publicToken cannot be null");

        LOGGER.info("Invoking Plaid API to retrieve an access token....");

        try {
            HttpHeaders httpHeaders = iRestUtil.getRestJSONHeader();
            String request = mapper.writeValueAsString(plaidAPIRequest);
            HttpEntity<String> requestHttpEntity = new HttpEntity<>(request, httpHeaders);

            // Build API URL for requesting Plaid Access Token and post for response
            String apiURL = plaidAPIConfig.getAPIPathURL("/item/public_token/exchange");
            AccessTokenResponse accessTokenResponse = restTemplate.postForObject(apiURL, requestHttpEntity, AccessTokenResponse.class);
            LOGGER.info("Invoking Plaid API to exchange for access token with URL:=> {}", apiURL);
            return Optional.of(accessTokenResponse);
        } catch (JsonProcessingException e) {
            LOGGER.warn("Failed to exchange Plaid public token for access token", e);
        }

        return Optional.empty();
    }

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
            LOGGER.info("Invoking Plaid API to create a Stripe Bank Acct token with URL:=> {}", apiURL);
            StripeBankAccountResponse accessTokenResponse = restTemplate.postForObject(apiURL, requestHttpEntity, StripeBankAccountResponse.class);
            return Optional.of(accessTokenResponse);
        } catch (JsonProcessingException e) {
            LOGGER.warn("Failed to exchange Plaid public token for access token", e);
        }

        return Optional.empty();
    }
}

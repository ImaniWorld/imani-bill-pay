package com.imani.bill.pay.service.payment.plaid;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imani.bill.pay.domain.payment.IHasPaymentInfo;
import com.imani.bill.pay.domain.payment.config.PlaidAPIConfig;
import com.imani.bill.pay.domain.payment.plaid.*;
import com.imani.bill.pay.domain.property.PropertyManager;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.service.rest.RestTemplateConfigurator;
import com.imani.bill.pay.service.util.IRestUtil;
import com.imani.bill.pay.service.util.RestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Qualifier(PlaidAPIEndPointFacade.SPRING_BEAN)
    private IPlaidAPIEndPointFacade iPlaidAPIEndPointFacade;

    @Autowired
    private ObjectMapper mapper;


    public static final String ITEM_ACCOUNTS_RETRIEVE_PATH = "/accounts/get";

    public static final String CREATE_STRIPE_BANK_TOKEN_PATH = "/processor/stripe/bank_account_token/create";


    public static final String SPRING_BEAN = "com.imani.bill.pay.service.payment.plaid.PlaidAPIService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PlaidAPIService.class);



    @Override
    public Optional<StripeBankAccountResponse> createStripeBankAccount(PlaidAPIRequest plaidAPIRequest, IHasPaymentInfo iHasPaymentInfo) {
        Assert.notNull(iHasPaymentInfo, "iHasPaymentInfo cannot be null");
        Assert.notNull(plaidAPIRequest, "PlaidAPIRequest cannot be null");
        Assert.notNull(plaidAPIRequest.getSecret(), "Plaid secret cannot be null");
        Assert.notNull(plaidAPIRequest.getClientID(), "Plaid clientID cannot be null");
        Assert.notNull(plaidAPIRequest.getAccessToken(), "Plaid accessToken cannot be null");
        Assert.notNull(plaidAPIRequest.getAccountID(), "Plaid accountID cannot be null");

        LOGGER.info("Attempting to create Stripe Bank account for iHasPaymentInfo :=> {}", iHasPaymentInfo);

        // Build PlaidAPIInvocationStatistic
        PlaidAPIInvocationStatistic plaidAPIInvocationStatistic = PlaidAPIInvocationStatistic.builder()
                .plaidAPIInvocationE(PlaidAPIInvocationE.StripeAcctCreate)
                .plaidAPIRequest(plaidAPIRequest)
                .build();
        setIHasPaymentInfo(plaidAPIInvocationStatistic, iHasPaymentInfo);

        // Build API URL for creating Plaid Stripe account id
        String apiURL = plaidAPIConfig.getAPIPathURL(CREATE_STRIPE_BANK_TOKEN_PATH);
        StripeBankAccountResponse stripeBankAccountResponse = new StripeBankAccountResponse();
        stripeBankAccountResponse = iPlaidAPIEndPointFacade.invokePlaidAPIEndPoint(plaidAPIInvocationStatistic, apiURL, stripeBankAccountResponse);
        return Optional.of(stripeBankAccountResponse);
    }

    @Override
    public Optional<PlaidItemAccountsResponse> getPlaidItemAccounts(PlaidAPIRequest plaidAPIRequest, IHasPaymentInfo iHasPaymentInfo) {
        Assert.notNull(plaidAPIRequest, "PlaidAPIRequest cannot be null");
        Assert.notNull(plaidAPIRequest.getSecret(), "Plaid secret cannot be null");
        Assert.notNull(iHasPaymentInfo, "IHasPaymentInfo cannot be null");

        LOGGER.info("Attempting to retrieve all Plaid Bank accounts currently associated with iHasPaymentInfo :=> {}", iHasPaymentInfo);

        // Build PlaidAPIInvocationStatistic
        PlaidAPIInvocationStatistic plaidAPIInvocationStatistic = PlaidAPIInvocationStatistic.builder()
                .plaidAPIInvocationE(PlaidAPIInvocationE.AccountsInfo)
                .plaidAPIRequest(plaidAPIRequest)
                .build();
        setIHasPaymentInfo(plaidAPIInvocationStatistic, iHasPaymentInfo);

        // Build API URL for getting all an Items accounts
        String apiURL = plaidAPIConfig.getAPIPathURL(ITEM_ACCOUNTS_RETRIEVE_PATH);
        PlaidItemAccountsResponse plaidItemAccountsResponse = new PlaidItemAccountsResponse();
        plaidItemAccountsResponse = iPlaidAPIEndPointFacade.invokePlaidAPIEndPoint(plaidAPIInvocationStatistic, apiURL, plaidItemAccountsResponse);
        return Optional.of(plaidItemAccountsResponse);
    }

    @Override
    public Optional<PlaidAccessTokenResponse> exchangePublicTokenForAccess(String plaidPublicToken, IHasPaymentInfo iHasPaymentInfo) {
        Assert.notNull(plaidPublicToken, "plaidPublicToken cannot be null");
        Assert.notNull(iHasPaymentInfo, "IHasPaymentInfo cannot be null");

        LOGGER.info("Exchanging Plaid public token for an access token related for iHasPaymentInfo :=> {}", iHasPaymentInfo);

        PlaidAPIRequest accessTokenAPIRequest = getAccessTokenAPIRequest(plaidPublicToken);

        PlaidAPIInvocationStatistic plaidAPIInvocationStatistic = PlaidAPIInvocationStatistic.builder()
                .plaidAPIRequest(accessTokenAPIRequest)
                .plaidAPIInvocationE(PlaidAPIInvocationE.AccessToken)
                .build();
        setIHasPaymentInfo(plaidAPIInvocationStatistic, iHasPaymentInfo);

        String apiURL = plaidAPIConfig.getAPIPathURL("/item/public_token/exchange");
        PlaidAccessTokenResponse plaidAccessTokenResponse = new PlaidAccessTokenResponse();
        plaidAccessTokenResponse = iPlaidAPIEndPointFacade.invokePlaidAPIEndPoint(plaidAPIInvocationStatistic, apiURL, plaidAccessTokenResponse);

        return Optional.of(plaidAccessTokenResponse);
    }


    void setIHasPaymentInfo(PlaidAPIInvocationStatistic plaidAPIInvocationStatistic, IHasPaymentInfo iHasPaymentInfo) {
        if(iHasPaymentInfo instanceof UserRecord) {
            plaidAPIInvocationStatistic.setUserRecord((UserRecord) iHasPaymentInfo);
        } else if(iHasPaymentInfo instanceof PropertyManager) {
            plaidAPIInvocationStatistic.setPropertyManager((PropertyManager) iHasPaymentInfo);
        }
    }


    PlaidAPIRequest getAccessTokenAPIRequest(String plaidPublicToken) {
        // Build param object to send request for access token
        PlaidAPIRequest plaidAPIRequest = PlaidAPIRequest.builder()
                .secret(plaidAPIConfig.getSecret())
                .clientID(plaidAPIConfig.getClientID())
                .publicToken(plaidPublicToken)
                .build();
        return plaidAPIRequest;
    }

}

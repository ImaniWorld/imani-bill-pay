package com.imani.bill.pay.service.payment.plaid;

import com.imani.bill.pay.domain.payment.config.PlaidAPIConfig;
import com.imani.bill.pay.domain.payment.plaid.PlaidAccessTokenResponse;
import com.imani.bill.pay.domain.payment.plaid.PlaidAPIRequest;
import com.imani.bill.pay.domain.payment.plaid.StripeBankAccountResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * @author manyce400
 */
@Service(PlaidAccountMasterService.SPRING_BEAN)
public class PlaidAccountMasterService implements IPlaidAccountMasterService {


    @Autowired
    private PlaidAPIConfig plaidAPIConfig;

    @Autowired
    @Qualifier(PlaidAPIService.SPRING_BEAN)
    private IPlaidAPIService iPlaidAPIService;


    public static final String SPRING_BEAN = "com.imani.bill.pay.service.payment.plaid.PlaidAccountMasterService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PlaidAccountMasterService.class);


    @Override
    public Optional<StripeBankAccountResponse> createStripeAccount(String plaidPublicToken, String plaidAccountID) {
        Assert.notNull(plaidPublicToken, "plaidPublicToken cannot be null");
        Assert.notNull(plaidAccountID, "plaidAccountID cannot be null");

        LOGGER.info("Creating Stripe Connected account for newly created Plaid AccountID:=> {}", plaidAccountID);

        // Step I: Using the Public Token for Plaid Account, we will create a request for an Access Token which can be used to perform actions against Plaid Account
        PlaidAPIRequest accessTokenAPIRequest = buildPlaidAPIRequestForAccessToken(plaidPublicToken);
        Optional<PlaidAccessTokenResponse> accessTokenResponse = iPlaidAPIService.exchangePublicTokenForAccess(accessTokenAPIRequest);

        if(accessTokenResponse.isPresent()) {
            return createStripeAccount(accessTokenResponse.get(), plaidAccountID);
        }

        LOGGER.warn("Failed to create an access token for Plaid Account, cannot create and link Stripe Account");
        return Optional.empty();
    }

    @Override
    public Optional<StripeBankAccountResponse> createStripeAccount(PlaidAccessTokenResponse plaidAccessTokenResponse, String plaidAccountID) {
        Assert.notNull(plaidAccessTokenResponse, "PlaidAccessTokenResponse cannot be null");
        Assert.notNull(plaidAccessTokenResponse.getAccessToken(), "Actual token cannot be null");
        Assert.notNull(plaidAccountID, "plaidAccountID cannot be null");

        LOGGER.debug("Using Plaid Account access token to create a matching Stripe Account.....");

        PlaidAPIRequest newStripeAcctAPIRequest = buildPlaidAPIRequestForStripeAccountCreate(plaidAccessTokenResponse.getAccessToken(), plaidAccountID);

        // We can now use the Access token to create a connected Stripe account for this Plaid account.
        Optional<StripeBankAccountResponse> stripeBankAccountResponse = iPlaidAPIService.createStripeBankAccount(newStripeAcctAPIRequest);
        return stripeBankAccountResponse;
    }

    PlaidAPIRequest buildPlaidAPIRequestForAccessToken(String plaidPublicToken) {
        PlaidAPIRequest plaidAPIRequest = PlaidAPIRequest.builder()
                .secret(plaidAPIConfig.getSecret())
                .clientID(plaidAPIConfig.getClientID())
                .publicToken(plaidPublicToken)
                .build();
        return plaidAPIRequest;
    }

    PlaidAPIRequest buildPlaidAPIRequestForStripeAccountCreate(String accessToken, String plaidAccountID) {
        PlaidAPIRequest plaidAPIRequest = PlaidAPIRequest.builder()
                .secret(plaidAPIConfig.getSecret())
                .clientID(plaidAPIConfig.getClientID())
                .accessToken(accessToken)
                .accountID(plaidAccountID)
                .build();
        return plaidAPIRequest;
    }
}

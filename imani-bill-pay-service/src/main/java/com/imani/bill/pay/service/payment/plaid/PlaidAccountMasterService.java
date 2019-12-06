package com.imani.bill.pay.service.payment.plaid;

import com.imani.bill.pay.domain.payment.config.PlaidAPIConfig;
import com.imani.bill.pay.domain.payment.plaid.AccessTokenResponse;
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

        LOGGER.info("Creating Stripe Account connected to newly created Plaid AccountID:=> {}", plaidAccountID);

        // Step 1: Token Exchange, use public token to fetch Plaid AccessToken.  AccessToken is required to create and access account details
        PlaidAPIRequest accessTokenAPIRequest = getPlaidAPIRequestForAccessToken(plaidPublicToken);

        Optional<AccessTokenResponse> accessTokenResponse = iPlaidAPIService.exchangePublicTokenForAccess(accessTokenAPIRequest);
        if(accessTokenResponse.isPresent()) {
            LOGGER.info("Successfully exchanged public linked Plaid token for an access token.");
            PlaidAPIRequest newStripeAcctAPIRequest = getPlaidAPIRequestForStripeAccountCreate(accessTokenResponse.get().getAccessToken(), plaidAccountID);
            Optional<StripeBankAccountResponse> stripeBankAccountResponse = iPlaidAPIService.createStripeBankAccount(newStripeAcctAPIRequest);
            return stripeBankAccountResponse;
        }

        return Optional.empty();
    }


    PlaidAPIRequest getPlaidAPIRequestForAccessToken(String plaidPublicToken) {
        PlaidAPIRequest plaidAPIRequest = PlaidAPIRequest.builder()
                .secret(plaidAPIConfig.getSecret())
                .clientID(plaidAPIConfig.getClientID())
                .publicToken(plaidPublicToken)
                .build();
        return plaidAPIRequest;
    }

    PlaidAPIRequest getPlaidAPIRequestForStripeAccountCreate(String accessToken, String plaidAccountID) {
        PlaidAPIRequest plaidAPIRequest = PlaidAPIRequest.builder()
                .secret(plaidAPIConfig.getSecret())
                .clientID(plaidAPIConfig.getClientID())
                .accessToken(accessToken)
                .accountID(plaidAccountID)
                .build();
        return plaidAPIRequest;
    }
}

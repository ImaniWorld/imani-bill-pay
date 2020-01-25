package com.imani.bill.pay.service.payment.plaid;

import com.imani.bill.pay.domain.payment.ACHPaymentInfo;
import com.imani.bill.pay.domain.payment.config.PlaidAPIConfig;
import com.imani.bill.pay.domain.payment.plaid.*;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.repository.IUserRecordRepository;
import com.imani.bill.pay.service.payment.ACHPaymentInfoService;
import com.imani.bill.pay.service.payment.IACHPaymentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.List;
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

    @Autowired
    private IUserRecordRepository iUserRecordRepository;

    @Autowired
    @Qualifier(ACHPaymentInfoService.SPRING_BEAN)
    private IACHPaymentInfoService iachPaymentInfoService;


    public static final String SPRING_BEAN = "com.imani.bill.pay.service.payment.plaid.PlaidAccountMasterService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PlaidAccountMasterService.class);


    @Transactional
    @Override
    public Optional<UserRecord> linkPlaidBankAcct(String plaidPublicToken, String plaidAccountID, UserRecord userRecord) {
        Assert.notNull(plaidPublicToken, "plaidPublicToken cannot be null");
        Assert.notNull(plaidAccountID, "plaidAccountID cannot be null");
        Assert.notNull(userRecord, "userRecord cannot be null");

        LOGGER.info("Attempting to create Imani PlaidBankAcct for linked plaidPublicToken:=> {} and plaidAccountID:=> {} for user:=> {}", plaidPublicToken, plaidAccountID, userRecord.getEmbeddedContactInfo().getEmail());

        // Load up actual user from DB.
        userRecord = iUserRecordRepository.findByUserEmail(userRecord.getEmbeddedContactInfo().getEmail());

        // 1: In order to access Plaid API's for this account, we will first need to request an access token which should be stored.
        Optional<PlaidAccessTokenResponse> accessTokenResponse = iPlaidAPIService.exchangePublicTokenForAccess(plaidPublicToken);
        if(accessTokenResponse.isPresent()) {
            LOGGER.info("Access token for account has been succesfully retrieved:=> ", accessTokenResponse.get().getAccessToken());

            // 2: Using access token we can now request all the details for this Plaid linked account
            PlaidAPIRequest plaidAPIRequest = buildPlaidAPIRequestForItemBankAccounts(accessTokenResponse.get().getAccessToken());
            Optional<PlaidItemAccountsResponse> plaidItemAccountsResponse = iPlaidAPIService.getPlaidItemAccounts(plaidAPIRequest);

            if(plaidItemAccountsResponse.isPresent()) {
                LOGGER.info("Successfully retrieved Plaid Account details");
                System.out.println("plaidItemAccountsResponse.get().getPlaidItemInfo() = " + plaidItemAccountsResponse.get().getPlaidItemInfo());

                // Get all accounts found internally
                List<PlaidBankAcct> accounts = plaidItemAccountsResponse.get().getAccounts();
                System.out.println("Total Size of accounts.size() = " + accounts.size());

                // Get the first account and display details
                PlaidBankAcct first = accounts.get(0);
                System.out.println("first = " + first);

                if (first != null) {
                    first.setPlaidAccessToken(accessTokenResponse.get().getAccessToken());
                    LOGGER.info("Linking Plaid Account completed, saving ACHPaymentInfo....");
                    ACHPaymentInfo achPaymentInfo = iachPaymentInfoService.buildPrimaryACHPaymentInfo(userRecord);
                    iachPaymentInfoService.updatePlaidBankAcct(first, achPaymentInfo);
                    iachPaymentInfoService.saveACHPaymentInfo(achPaymentInfo);
                }
            }
        }
        return Optional.empty();
    }

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


    PlaidAPIRequest buildPlaidAPIRequestForItemBankAccounts(String accessToken) {
        PlaidAPIRequest plaidAPIRequest = PlaidAPIRequest.builder()
                .secret(plaidAPIConfig.getSecret())
                .clientID(plaidAPIConfig.getClientID())
                .accessToken(accessToken)
                .build();
        return plaidAPIRequest;
    }


    @PostConstruct
    void postConstruct() {
        LOGGER.info("===================  Running Plaid API with configuration =======================");
        LOGGER.info("{}", plaidAPIConfig);
        LOGGER.info("=================================================================================");
    }

}

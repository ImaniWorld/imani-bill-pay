package com.imani.bill.pay.service.payment.plaid;

import com.imani.bill.pay.domain.execution.ExecutionError;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.execution.ValidationAdvice;
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
import org.springframework.util.StringUtils;

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
    public ExecutionResult linkPlaidBankAcct(String plaidPublicToken, UserRecord userRecord) {
        Assert.notNull(plaidPublicToken, "plaidPublicToken cannot be null");
        Assert.notNull(userRecord, "userRecord cannot be null");

        LOGGER.info("Attempting to create Imani PlaidBankAcct for linked plaidPublicToken:=> {} for user:=> {}", plaidPublicToken, userRecord.getEmbeddedContactInfo().getEmail());

        // Create ExecutionResult and begin validating userRecord
        ExecutionResult executionResult = new ExecutionResult();
        if(StringUtils.isEmpty(plaidPublicToken)) {
            executionResult.addValidationAdvice(ValidationAdvice.newInstance("Required PlaidPublicToken was not provided"));
        }

        // Load up actual user from DB.
        userRecord = iUserRecordRepository.findByUserEmail(userRecord.getEmbeddedContactInfo().getEmail());

        // 1: In order to access Plaid API's for this account, we will first need to request an access token which should be stored.
        Optional<PlaidAccessTokenResponse> accessTokenResponse = iPlaidAPIService.exchangePublicTokenForAccess(plaidPublicToken, userRecord);
        if(accessTokenResponse.isPresent() && !accessTokenResponse.get().hasError()) {
            LOGGER.info("Access token for account has been succesfully retrieved:=> {}", accessTokenResponse.get().getAccessToken());

            // 2: Using access token we can now request all the details for this Plaid linked account
            PlaidAPIRequest plaidAPIRequest = buildPlaidAPIRequestForItemBankAccounts(accessTokenResponse.get().getAccessToken());
            Optional<PlaidItemAccountsResponse> plaidItemAccountsResponse = iPlaidAPIService.getPlaidItemAccounts(plaidAPIRequest, userRecord);

            if(plaidItemAccountsResponse.isPresent() && !plaidItemAccountsResponse.get().hasError()) {
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
                    iachPaymentInfoService.updateAndSavePlaidBankAcct(first, achPaymentInfo);
                }
            }
        } else {
            LOGGER.warn("Required Plaid access token could not be retrieved, abandoning link operation.");
            executionResult.addValidationAdvice(ValidationAdvice.newInstance("Failed to verify and link provided Bank account"));
        }


        return executionResult;
    }


    @Transactional
    @Override
    public ExecutionResult createStripeAcctForPrimaryPlaidAcct(UserRecord userRecord) {
        Assert.notNull(userRecord, "UserRecord cannot be null");
        Assert.notNull(userRecord.getEmbeddedContactInfo(), "EmbeddedContactInfo cannot be null");

        LOGGER.info("Attempting to create Stripe and link Stripe BankAcct for primary payment for user:=> {}", userRecord.getEmbeddedContactInfo().getEmail());

        ExecutionResult executionResult = new ExecutionResult();

        // Fetch user from DB for consistency and find primary ACHPayment Information
        userRecord = iUserRecordRepository.findByUserEmail(userRecord.getEmbeddedContactInfo().getEmail());
        ACHPaymentInfo achPaymentInfo = iachPaymentInfoService.findUserPrimaryPamentInfo(userRecord);

        if(achPaymentInfo != null) {
            // Verify that a Stripe Bank AcctID hasn't already been created and create.
            if(achPaymentInfo.getStripeBankAcct() == null
                    || achPaymentInfo.getStripeBankAcct().getBankAcctToken() == null) {
                PlaidAPIRequest plaidStripeAcctCreateRequest = buildPlaidAPIRequestForStripeAccountCreate(achPaymentInfo);
                Optional<StripeBankAccountResponse> stripeBankAccountResponse = iPlaidAPIService.createStripeBankAccount(plaidStripeAcctCreateRequest, userRecord);

                if(stripeBankAccountResponse.isPresent() && !stripeBankAccountResponse.get().hasError()) {
                    LOGGER.info("Successfully created and linked Stripe Bank Account generated Stripe Token:=> {}", stripeBankAccountResponse.get().getStripeBankAcctToken());
                    achPaymentInfo.updateStripeBankAcctToken(stripeBankAccountResponse.get().getStripeBankAcctToken());
                    iachPaymentInfoService.saveACHPaymentInfo(achPaymentInfo);
                    return executionResult;
                } else {
                    LOGGER.warn("Plaid call to create Stripe Bank Account token returned no results, check Plaid invocation results.");
                    executionResult.addExecutionError(ExecutionError.of("Failed to retrieve required Stripe BankAccount token from Plaid"));
                }
            } else {
                LOGGER.warn("Failed to find the primary ACHPaymentInfo for user, cannot create and link a Stripe Bank account.  This means no Plaid Account has been linked.");
                executionResult.addExecutionError(ExecutionError.of("Failed to retrieve required Stripe BankAccount token from Plaid"));
            }
        }

        return executionResult;
    }

    PlaidAPIRequest buildPlaidAPIRequestForAccessToken(String plaidPublicToken) {
        PlaidAPIRequest plaidAPIRequest = PlaidAPIRequest.builder()
                .secret(plaidAPIConfig.getSecret())
                .clientID(plaidAPIConfig.getClientID())
                .publicToken(plaidPublicToken)
                .build();
        return plaidAPIRequest;
    }

    PlaidAPIRequest buildPlaidAPIRequestForStripeAccountCreate(ACHPaymentInfo achPaymentInfo) {
        PlaidAPIRequest plaidAPIRequest = PlaidAPIRequest.builder()
                .secret(plaidAPIConfig.getSecret())
                .clientID(plaidAPIConfig.getClientID())
                .accessToken(achPaymentInfo.getPlaidBankAcct().getPlaidAccessToken())
                .accountID(achPaymentInfo.getPlaidBankAcct().getAccountID())
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

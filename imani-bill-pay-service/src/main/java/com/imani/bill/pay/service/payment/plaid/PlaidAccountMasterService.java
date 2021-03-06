package com.imani.bill.pay.service.payment.plaid;

import com.imani.bill.pay.domain.execution.ExecutionError;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.execution.ValidationAdvice;
import com.imani.bill.pay.domain.payment.ACHPaymentInfo;
import com.imani.bill.pay.domain.payment.IHasPaymentInfo;
import com.imani.bill.pay.domain.payment.config.PlaidAPIConfig;
import com.imani.bill.pay.domain.payment.plaid.*;
import com.imani.bill.pay.domain.property.PropertyManager;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.repository.IUserRecordRepository;
import com.imani.bill.pay.service.payment.ACHPaymentInfoService;
import com.imani.bill.pay.service.payment.IACHPaymentInfoService;
import com.imani.bill.pay.service.payment.security.PlaidAccessTokenSecurityAdviseService;
import com.imani.bill.pay.service.property.manager.IPropertyManagerService;
import com.imani.bill.pay.service.property.manager.PropertyManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    // TODO always inject service beans
    @Autowired
    private IUserRecordRepository iUserRecordRepository;

    @Autowired
    @Qualifier(PropertyManagerService.SPRING_BEAN)
    private IPropertyManagerService iPropertyManagerService;

    @Autowired
    @Qualifier(ACHPaymentInfoService.SPRING_BEAN)
    private IACHPaymentInfoService iachPaymentInfoService;

    @Autowired
    private PlaidAccessTokenSecurityAdviseService plaidAccessTokenSecurityAdviseService;


    public static final String SPRING_BEAN = "com.imani.bill.pay.service.payment.plaid.PlaidAccountMasterService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PlaidAccountMasterService.class);


    @Transactional
    @Override
    public ExecutionResult linkPlaidBankAcct(String plaidPublicToken, String userID) {
        // TODO find a way where user doesnt have to be loaded 2x
        UserRecord userRecord = iUserRecordRepository.findByUserEmail(userID);
        return linkPlaidBankAcct(plaidPublicToken, userRecord);
    }

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

        // Load up actual UserRecord from DB and complete the link operation
        userRecord = iUserRecordRepository.findByUserEmail(userRecord.getEmbeddedContactInfo().getEmail());
        completeStripeAcctLink(plaidPublicToken, userRecord, executionResult);
        return executionResult;
    }

    @Transactional
    @Override
    public ExecutionResult linkPlaidBankAcct(String plaidPublicToken, PropertyManager propertyManager) {
        Assert.notNull(plaidPublicToken, "Plaid Public Token cannot be null");
        Assert.notNull(propertyManager, "PropertyManager cannot be null");

        LOGGER.info("Attempting to create Imani PlaidBankAcct for linked plaidPublicToken:=> {} for propertyManager:=> {}", plaidPublicToken, propertyManager.getName());


        ExecutionResult executionResult = new ExecutionResult();

        // Load up actual PropertyManager from DB and complete the link operation
        propertyManager = iPropertyManagerService.findByEmail(propertyManager.getEmbeddedContactInfo().getEmail());
        completeStripeAcctLink(plaidPublicToken, propertyManager, executionResult);
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
        ACHPaymentInfo achPaymentInfo = iachPaymentInfoService.findPrimaryPamentInfo(userRecord);

        if(achPaymentInfo != null) {
            // Verify that a Stripe Bank AcctID hasn't already been created and create.
            if(!achPaymentInfo.hasStripeBankAcct()) {
                PlaidAPIRequest plaidStripeAcctCreateRequest = buildPlaidAPIRequestForStripeAccountCreate(achPaymentInfo);

                // Invoke Plaid APIs to create and link a Stripe Bank account token.  This token can then be used to create and associate a Stripe Customer in future step
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
                LOGGER.warn("Failed to find the primary ACHPaymentInfo for user, cannot create and link a Stripe Bank account.  This means no Plaid Account has been created for user");
                executionResult.addExecutionError(ExecutionError.of("User has not successfully validated their Plaid Bank Account"));
            }
        }

        return executionResult;
    }

    /**
     * This method is the main place that a user's ACHPaymentInfo is initially created by executing below steps.
     *
     * <ol>
     *     <li>Use the Plaid Public Account token which is generated after a user logs in to exchange for a secure access token</li>
     *     <li>Once we have the access token, we use that to fetch all details about the Plaid linked bank account to store locally</li>
     *     <li>With all these details ACHPaymentInfo is built and the secure token is stored so we can use that in future requests</li>
     * </ol>
     *
     * @param plaidPublicToken
     * @param iHasPaymentInfo
     * @param executionResult
     */
    void completeStripeAcctLink(String plaidPublicToken, IHasPaymentInfo iHasPaymentInfo, ExecutionResult executionResult) {
        // Step 1: In order to access Plaid API's for this account, we will first need to request an access token which should be stored with ACHPaymentInfo

        // Get security advice on if user has exceeded number of times to perform this action
        Set<ValidationAdvice> tokenAccessSecurityAdvice = plaidAccessTokenSecurityAdviseService.getAdvice(iHasPaymentInfo);

        if (CollectionUtils.isEmpty(tokenAccessSecurityAdvice)) {
            Optional<PlaidAccessTokenResponse> accessTokenResponse = iPlaidAPIService.exchangePublicTokenForAccess(plaidPublicToken, iHasPaymentInfo);

            if(accessTokenResponse.isPresent() && !accessTokenResponse.get().hasError()) {
                LOGGER.info("Plaid Secure access token for account has been successfully retrieved, attempting to retrieve account details...");

                // 2: Using access token we can now request all the details for this Plaid linked account
                PlaidAPIRequest plaidAPIRequest = buildPlaidAPIRequestForItemBankAccounts(accessTokenResponse.get().getAccessToken());
                Optional<PlaidItemAccountsResponse> plaidItemAccountsResponse = iPlaidAPIService.getPlaidItemAccounts(plaidAPIRequest, iHasPaymentInfo);

                if(plaidItemAccountsResponse.isPresent() && !plaidItemAccountsResponse.get().hasError()) {
                    LOGGER.info("Successfully retrieved Plaid Account details");

                    // By default our Plaid Link integration should allow the selection of only one account so safe to get first.
                    List<PlaidBankAcct> accounts = plaidItemAccountsResponse.get().getAccounts();
                    PlaidBankAcct selectedPlaidBankAcct = accounts.get(0);

                    if (selectedPlaidBankAcct != null) {
                        selectedPlaidBankAcct.setPlaidAccessToken(accessTokenResponse.get().getAccessToken());
                        LOGGER.info("Linking Plaid Account completed, saving ACHPaymentInfo....");
                        ACHPaymentInfo achPaymentInfo = iachPaymentInfoService.buildPrimaryACHPaymentInfo(iHasPaymentInfo);
                        iachPaymentInfoService.updateAndSavePlaidBankAcct(selectedPlaidBankAcct, achPaymentInfo);
                    }
                }
            } else {
                LOGGER.warn("Required Plaid access token could not be retrieved, abandoning link operation.");
                executionResult.addValidationAdvice(ValidationAdvice.newInstance("Failed to verify and link provided Bank account"));
            }
        }

        executionResult.addValidationAdvices(tokenAccessSecurityAdvice);
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


//    @PostConstruct
//    void postConstruct() {
//        LOGGER.info("===================  Running Plaid API with configuration =======================");
//        LOGGER.info("{}", plaidAPIConfig);
//        LOGGER.info("=================================================================================");
//    }

}

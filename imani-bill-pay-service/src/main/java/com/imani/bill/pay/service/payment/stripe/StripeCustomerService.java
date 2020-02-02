package com.imani.bill.pay.service.payment.stripe;

import com.imani.bill.pay.domain.execution.ExecutionError;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.payment.ACHPaymentInfo;
import com.imani.bill.pay.domain.payment.config.PlaidAPIConfig;
import com.imani.bill.pay.domain.payment.config.StripeAPIConfig;
import com.imani.bill.pay.domain.payment.plaid.PlaidAPIRequest;
import com.imani.bill.pay.domain.payment.stripe.CustomerObjFieldsE;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.repository.IUserRecordRepository;
import com.imani.bill.pay.service.payment.ACHPaymentInfoService;
import com.imani.bill.pay.service.payment.IACHPaymentInfoService;
import com.imani.bill.pay.service.payment.plaid.IPlaidAPIService;
import com.imani.bill.pay.service.payment.plaid.IPlaidAccountMasterService;
import com.imani.bill.pay.service.payment.plaid.PlaidAPIService;
import com.imani.bill.pay.service.payment.plaid.PlaidAccountMasterService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.BankAccount;
import com.stripe.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author manyce400
 */
@Service(StripeCustomerService.SPRING_BEAN)
public class StripeCustomerService implements IStripeCustomerService {


    @Autowired
    private StripeAPIConfig stripeAPIConfig;

    @Autowired
    private PlaidAPIConfig plaidAPIConfig;

    @Autowired
    private IUserRecordRepository iUserRecordRepository;

    @Autowired
    @Qualifier(PlaidAPIService.SPRING_BEAN)
    private IPlaidAPIService iPlaidAPIService;

    @Autowired
    @Qualifier(ACHPaymentInfoService.SPRING_BEAN)
    private IACHPaymentInfoService iachPaymentInfoService;

    @Autowired
    @Qualifier(PlaidAccountMasterService.SPRING_BEAN)
    private IPlaidAccountMasterService iPlaidAccountMasterService;


    private static final String EMAIL_CUSTOMER_PARAM = "email";

    private static final String BANK_ACCT_SOURCE_CUSTOMER_PARAM = "source";


    public static final String SPRING_BEAN = "com.imani.bill.pay.service.payment.stripe.StripeCustomerService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(StripeCustomerService.class);

    /**
     * Create and return a Stripe Customer
     * @param userRecord
     * @return
     */
    @Override
    @Transactional
    public Optional<Customer> createStripeCustomer(UserRecord userRecord) {
        Assert.notNull(userRecord, "UserRecord cannot be null");
        //Assert.isTrue(userRecord.getUserRecordTypeE().isEndUser(), "Stripe Customer can only be created for End Users only.");
        Assert.notNull(userRecord.getEmbeddedContactInfo(), "EmbeddedContactInfo cannot be null");
        LOGGER.info("Creating Stripe Customer object reference for user:=> {}", userRecord.getEmbeddedContactInfo().getEmail());

        Stripe.apiKey = stripeAPIConfig.getApiKey();

        Map<String, Object> params = CustomerObjFieldsE.getCustomerCreateParams(userRecord);
        LOGGER.info("Params to be used in creating customer:=> {}", params);

        try {
            Customer customer = Customer.create(params);
            LOGGER.info("Successfully Created Stripe Customer, updating UserRecord with Id:=> {}", customer.getId());
            userRecord.setStripeCustomerID(customer.getId());
            iUserRecordRepository.save(userRecord);
            return Optional.of(customer);
        } catch (StripeException e) {
            LOGGER.warn("Failed to create Stripe Customer object reference, user will not be able to make payments", e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Customer> retrieveStripeCustomer(UserRecord userRecord) {
        Assert.notNull(userRecord, "UserRecord cannot be null");
        //Assert.isTrue(userRecord.getUserRecordTypeE().isEndUser(), "Stripe Customer can only be created for End Users only.");

        Stripe.apiKey = stripeAPIConfig.getApiKey();

        LOGGER.info("Attempting to retrieve existing Stripe Customer object for for user:=> {}", userRecord.getEmbeddedContactInfo().getEmail());

        try {
            Customer stripeCustomer = Customer.retrieve(userRecord.getStripeCustomerID());
            return Optional.of(stripeCustomer);
        } catch (StripeException e) {
            LOGGER.warn("Failed to retrieve Stripe Customer for user:=> {}", userRecord.getEmbeddedContactInfo().getEmail());
        }

        return Optional.empty();
    }


    @Override
    public boolean deleteStripeCustomer(UserRecord userRecord) {
        Assert.notNull(userRecord, "UserRecord cannot be null");

        Stripe.apiKey = stripeAPIConfig.getApiKey();

        LOGGER.info("Attempting to delete existing Stripe Customer object for for user:=> {}", userRecord.getEmbeddedContactInfo().getEmail());

        // First retrieve the Stripe Customer object for this UserRecord
        Optional<Customer> stripeCustomer = retrieveStripeCustomer(userRecord);

        if(stripeCustomer.isPresent()) {
            try {
                stripeCustomer.get().delete();
                return true;
            } catch (StripeException e) {
                LOGGER.warn("Exception occurred while trying to delete existing Stripe Customer", e);
            }
        }

        return false;
    }

    @Transactional
    @Override
    public ExecutionResult updatePrimaryStripeCustomerBankAcct(UserRecord userRecord) {
        Assert.notNull(userRecord, "UserRecord cannot be null");

        LOGGER.info("Attempting to update Stripe Customer bank account for user:=> {}");

        // Fetch UserRecord from persistent store
        userRecord = iUserRecordRepository.findByUserEmail(userRecord.getEmbeddedContactInfo().getEmail());
        Optional<Customer> stripeCustomer = retrieveStripeCustomer(userRecord);

        ExecutionResult executionResult = new ExecutionResult();

        if(stripeCustomer.isPresent()) {
            LOGGER.info("Existing Stripe customer found, proceeding to update bank accounts....");
            Stripe.apiKey = stripeAPIConfig.getApiKey();

            ACHPaymentInfo achPaymentInfo = iachPaymentInfoService.findUserPrimaryPamentInfo(userRecord);

            if (achPaymentInfo != null) {
                Map<String, Object> params = new HashMap<>();
                params.put(BANK_ACCT_SOURCE_CUSTOMER_PARAM, achPaymentInfo.getStripeBankAcct().getBankAcctToken());
                try {
                    BankAccount bankAccount = (BankAccount) stripeCustomer.get().getSources().create(params);
                    iachPaymentInfoService.updateStripeBankAcct(bankAccount, achPaymentInfo);
                    iachPaymentInfoService.saveACHPaymentInfo(achPaymentInfo);
                } catch (StripeException e) {
                    LOGGER.warn("Failed to update Stripe Bank account details for customer", e);
                    executionResult.addExecutionError(ExecutionError.of("Failed to create and link Stripe Bank account to the Customer object"));
                }
            } else {
                LOGGER.warn("No existing ACHPaymentInfo could be found for this user, abandoning Stripe bank account update.");
                executionResult.addExecutionError(ExecutionError.of("Cannot create or find an existing Stripe Customer"));
            }
        } else {
            LOGGER.warn("Failed to create or find an existing linked Stripe Customer object, abandoning adding Stripe Bank information.");
            executionResult.addExecutionError(ExecutionError.of("No existing ACHPaymentInfo found for user."));
        }

        return executionResult;
    }

    @Transactional
    @Override
    public ExecutionResult createPlaidStripeCustomerBankAcct(UserRecord userRecord) {
        Assert.notNull(userRecord, "UserRecord cannot be null");
        Assert.notNull(userRecord.getEmbeddedContactInfo(), "EmbeddedContactInfo cannot be null");
        //Assert.isTrue(userRecord.getUserRecordTypeE().isEndUser(), "Stripe Customer can only be created for End Users only.");

        // Fetch UserRecord from persistent store
        userRecord = iUserRecordRepository.findByUserEmail(userRecord.getEmbeddedContactInfo().getEmail());

        Optional<Customer> stripeCustomer;
        ExecutionResult executionResult = new ExecutionResult();

        if (userRecord.getStripeCustomerID() == null) {
            stripeCustomer = createStripeCustomer(userRecord);
        } else {
            stripeCustomer = retrieveStripeCustomer(userRecord);
        }

        if(stripeCustomer.isPresent()) {
            LOGGER.info("Existing Stripe customer found, proceeding to update bank accounts....");
            Stripe.apiKey = stripeAPIConfig.getApiKey();

            ACHPaymentInfo achPaymentInfo = iachPaymentInfoService.findUserPrimaryPamentInfo(userRecord);

            if (achPaymentInfo != null) {
                Map<String, Object> params = new HashMap<>();
                params.put(BANK_ACCT_SOURCE_CUSTOMER_PARAM, achPaymentInfo.getStripeBankAcct().getBankAcctToken());
                try {
                    BankAccount bankAccount = (BankAccount) stripeCustomer.get().getSources().create(params);
                    iachPaymentInfoService.updateStripeBankAcct(bankAccount, achPaymentInfo);
                    iachPaymentInfoService.saveACHPaymentInfo(achPaymentInfo);
                } catch (StripeException e) {
                    LOGGER.warn("Failed to update Stripe Bank account details for customer", e);
                    executionResult.addExecutionError(ExecutionError.of("Failed to create and link Stripe Bank account to the Customer object"));
                }
            }
        } else {
            LOGGER.warn("Failed to create or find an existing linked Stripe Customer object, abandoning adding Stripe Bank information.");
            executionResult.addExecutionError(ExecutionError.of("Cannot create or find an existing Stripe Customer"));
        }

        LOGGER.info("Stripe Customer or ACHPaymentInfo could not be found");
        return executionResult;
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
        LOGGER.info("===================  Running Stripe API with configuration =======================");
        LOGGER.info("{}", stripeAPIConfig);
        LOGGER.info("=================================================================================");
    }
}

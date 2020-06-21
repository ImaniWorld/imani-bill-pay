package com.imani.bill.pay.service.payment.stripe;

import com.imani.bill.pay.domain.execution.ExecutionError;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.payment.ACHPaymentInfo;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.repository.IUserRecordRepository;
import com.imani.bill.pay.service.payment.ACHPaymentInfoService;
import com.imani.bill.pay.service.payment.IACHPaymentInfoService;
import com.stripe.exception.StripeException;
import com.stripe.model.BankAccount;
import com.stripe.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

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
    @Qualifier(StripeCustomerAPIFacade.SPRING_BEAN)
    private IStripeCustomerAPIFacade iStripeCustomerAPIFacade;

    @Autowired
    private IUserRecordRepository iUserRecordRepository;

    @Autowired
    @Qualifier(ACHPaymentInfoService.SPRING_BEAN)
    private IACHPaymentInfoService iachPaymentInfoService;


    private static final String BANK_ACCT_SOURCE_CUSTOMER_PARAM = "source";


    public static final String SPRING_BEAN = "com.imani.bill.pay.service.payment.stripe.StripeCustomerService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(StripeCustomerService.class);


    @Transactional
    @Override
    public ExecutionResult updatePrimaryStripeCustomerBankAcct(UserRecord userRecord) {
        Assert.notNull(userRecord, "UserRecord cannot be null");

        LOGGER.info("Attempting to update Stripe Customer bank account for user:=> {}");

        // Fetch UserRecord from persistent store
        userRecord = iUserRecordRepository.findByUserEmail(userRecord.getEmbeddedContactInfo().getEmail());
        Optional<Customer> stripeCustomer = iStripeCustomerAPIFacade.retrieveStripeCustomer(userRecord);

        ExecutionResult executionResult = new ExecutionResult();

        if(stripeCustomer.isPresent()) {
            LOGGER.info("Existing Stripe customer found, proceeding to update bank accounts....");

            ACHPaymentInfo achPaymentInfo = iachPaymentInfoService.findPrimaryPamentInfo(userRecord);

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

    /**
     * Final step in creating an Imani BillPay user payment Stripe account.  This step should only be called after we have successfully validated
     * user's bank account details using our Plaid integration.  This step will hand over the validated Plaid Bank account to be linked with a newly created
     * Stripe Customer account.
     */
    @Transactional
    @Override
    public ExecutionResult createPlaidStripeCustomerBankAcct(UserRecord userRecord) {
        Assert.notNull(userRecord, "UserRecord cannot be null");
        Assert.notNull(userRecord.getEmbeddedContactInfo(), "EmbeddedContactInfo cannot be null");

        // Fetch UserRecord from persistent store
        userRecord = iUserRecordRepository.findByUserEmail(userRecord.getEmbeddedContactInfo().getEmail());

        Optional<Customer> stripeCustomer;
        ExecutionResult executionResult = new ExecutionResult();

        if (StringUtils.isEmpty(userRecord.getStripeCustomerID() )) {
            stripeCustomer = iStripeCustomerAPIFacade.createStripeCustomer(userRecord);
        } else {
            stripeCustomer = iStripeCustomerAPIFacade.retrieveStripeCustomer(userRecord);
        }

        if(stripeCustomer.isPresent()) {

            if(StringUtils.isEmpty(userRecord.getStripeCustomerID())) {
                // IF Stripe CustomerID has not been set, do so and save
                userRecord.setStripeCustomerID(stripeCustomer.get().getId());
                iUserRecordRepository.save(userRecord);
            }

            LOGGER.info("Proceeding to update bank account information on Stripe Customer account ....");

            ACHPaymentInfo achPaymentInfo = iachPaymentInfoService.findPrimaryPamentInfo(userRecord);

            if (achPaymentInfo != null
                    && achPaymentInfo.getStripeBankAcct() != null
                    && !StringUtils.isEmpty(achPaymentInfo.getStripeBankAcct().getBankAcctToken())) {
                Map<String, Object> params = new HashMap<>();
                params.put(BANK_ACCT_SOURCE_CUSTOMER_PARAM, achPaymentInfo.getStripeBankAcct().getBankAcctToken());
                Optional<BankAccount> bankAccount = iStripeCustomerAPIFacade.createBankAccount(stripeCustomer.get(), params);

                if(bankAccount.isPresent()) {
                    iachPaymentInfoService.updateStripeBankAcct(bankAccount.get(), achPaymentInfo);
                    iachPaymentInfoService.saveACHPaymentInfo(achPaymentInfo);
                } else {
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


//    @PostConstruct
//    void postConstruct() {
//        LOGGER.info("===================  Running Stripe API with configuration =======================");
//        LOGGER.info("{}", stripeAPIConfig);
//        LOGGER.info("=================================================================================");
//    }
}

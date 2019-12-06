package com.imani.bill.pay.service.payment.stripe;

import com.imani.bill.pay.domain.payment.config.StripeAPIConfig;
import com.imani.bill.pay.domain.payment.plaid.StripeBankAccountResponse;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.service.payment.plaid.IPlaidAccountMasterService;
import com.imani.bill.pay.service.payment.plaid.PlaidAccountMasterService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.BankAccount;
import com.stripe.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

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
    @Qualifier(PlaidAccountMasterService.SPRING_BEAN)
    private IPlaidAccountMasterService iPlaidAccountMasterService;


    private static final String EMAIL_CUSTOMER_PARAM = "email";

    private static final String BANK_ACCT_SOURCE_CUSTOMER_PARAM = "source";


    public static final String SPRING_BEAN = "com.imani.bill.pay.service.payment.stripe.StripeCustomerService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(StripeCustomerService.class);

    /**
     * Create and return a Stripe Customer ID
     * @param userRecord
     * @return
     */
    @Override
    public Optional<Customer> createStripeCustomer(UserRecord userRecord) {
        Assert.notNull(userRecord, "UserRecord cannot be null");
        Assert.notNull(userRecord.getEmbeddedContactInfo(), "EmbeddedContactInfo cannot be null");
        LOGGER.info("Creating Stripe Customer object reference for user:=> {}", userRecord.getEmbeddedContactInfo().getEmail());

        Stripe.apiKey = stripeAPIConfig.getApiKey();

        Map<String, Object> params = new HashMap<>();
        params.put(EMAIL_CUSTOMER_PARAM, userRecord.getEmbeddedContactInfo().getEmail());

        try {
            Customer customer = Customer.create(params);
            return Optional.of(customer);
        } catch (StripeException e) {
            LOGGER.warn("Failed to create Stripe Customer object reference, user will not be able to make payments", e);
        }

        return Optional.empty();
    }


    @Override
    public Optional<BankAccount> createStripeCustomerBankAcct(UserRecord userRecord, String plaidPublicToken, String plaidAccountID) {
        Assert.notNull(userRecord, "UserRecord cannot be null");
        Assert.notNull(userRecord.getEmbeddedContactInfo(), "EmbeddedContactInfo cannot be null");
        Assert.notNull(plaidPublicToken, "achPaymentInfo cannot be null");
        Assert.notNull(plaidAccountID, "plaidAccountID cannot be null");

        LOGGER.info("Creating Stripe Customer BankAcct for user:=> {}", userRecord.getEmbeddedContactInfo().getEmail());

        // First create Stripe Customer for this UserRecord
        Optional<Customer> stripeCustomer = createStripeCustomer(userRecord);

        if(stripeCustomer.isPresent()) {
            Stripe.apiKey = stripeAPIConfig.getApiKey();

            // We can now attempt to create a Stripe BankAcct that links to selected plaidAccountID
            Optional<StripeBankAccountResponse> stripeBankAccountResponse = iPlaidAccountMasterService.createStripeAccount(plaidPublicToken, plaidAccountID);
            if(stripeBankAccountResponse.isPresent()) {
                LOGGER.info("Successfully retrieved a created StripeBank Token associated with created Plaid Linked AcctID, creating Customer BankAcct...");
                Map<String, Object> params = new HashMap<>();
                params.put(BANK_ACCT_SOURCE_CUSTOMER_PARAM, stripeBankAccountResponse.get().getStripeBankAcctToken());

                try {
                    BankAccount bankAccount = (BankAccount) stripeCustomer.get().getSources().create(params);
                    Optional.of(bankAccount);
                } catch (StripeException e) {
                    LOGGER.warn("Failed to create Stripe BankAcct for UserRecord on Linked Plaid Account", e);
                }
            }
        }

        return Optional.empty();
    }
}

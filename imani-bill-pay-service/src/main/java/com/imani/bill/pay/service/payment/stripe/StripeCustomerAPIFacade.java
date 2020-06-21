package com.imani.bill.pay.service.payment.stripe;

import com.imani.bill.pay.domain.payment.config.StripeAPIConfig;
import com.imani.bill.pay.domain.payment.stripe.CustomerObjFieldsE;
import com.imani.bill.pay.domain.user.UserRecord;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.BankAccount;
import com.stripe.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Optional;

/**
 * @author manyce400
 */
@Service(StripeCustomerAPIFacade.SPRING_BEAN)
public class StripeCustomerAPIFacade implements IStripeCustomerAPIFacade {


    @Autowired
    private StripeAPIConfig stripeAPIConfig;

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.payment.stripe.StripeCustomerAPIFacade";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(StripeCustomerAPIFacade.class);

    @Override
    public Optional<Customer> createStripeCustomer(UserRecord userRecord) {
        Assert.notNull(userRecord, "UserRecord cannot be null");
        LOGGER.info("Creating Stripe Customer object reference for user:=> {}", userRecord.getEmbeddedContactInfo().getEmail());

        // Initialize Stripe Client API before invoking call
        Stripe.apiKey = stripeAPIConfig.getApiKey();

        Map<String, Object> params = CustomerObjFieldsE.getCustomerCreateParams(userRecord);
        LOGGER.info("Params to be used in creating customer:=> {}", params);

        try {
            Customer customer = Customer.create(params);
            LOGGER.info("Successfully created Stripe Customer with id:=> {}", customer.getId());
            return Optional.of(customer);
        } catch (StripeException e) {
            LOGGER.warn("Failed to create Stripe Customer object reference, user will not be able to make payments", e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Customer> retrieveStripeCustomer(UserRecord userRecord) {
        Assert.notNull(userRecord, "UserRecord cannot be null");
        LOGGER.info("Attempting to retrieve existing Stripe Customer object for for user:=> {}", userRecord.getEmbeddedContactInfo().getEmail());

        // Initialize Stripe Client API before invoking call
        Stripe.apiKey = stripeAPIConfig.getApiKey();

        try {
            Customer stripeCustomer = Customer.retrieve(userRecord.getStripeCustomerID());
            return Optional.of(stripeCustomer);
        } catch (StripeException e) {
            LOGGER.warn("Failed to retrieve Stripe Customer for user:=> {}", userRecord.getEmbeddedContactInfo().getEmail());
        }

        return Optional.empty();
    }

    @Override
    public Optional<BankAccount> createBankAccount(Customer customer, Map<String, Object> params) {
        Assert.notNull(customer, "customer cannot be null");
        Assert.notNull(params, "UserRecord cannot be null");
        Assert.isTrue(!params.isEmpty(), "Params cannot be empty");
        LOGGER.info("Attempting to Stripe bank account details for customer with id:=> {}", customer.getId());

        // Initialize Stripe Client API before invoking call
        Stripe.apiKey = stripeAPIConfig.getApiKey();

        try {
            BankAccount bankAccount = (BankAccount) customer.getSources().create(params);
            return Optional.of(bankAccount);
        } catch (StripeException e) {
            LOGGER.warn("Failed to update Stripe Bank account details for customer", e);
        }

        return Optional.empty();
    }

    @Override
    public boolean deleteStripeCustomer(UserRecord userRecord) {
        Assert.notNull(userRecord, "UserRecord cannot be null");
        LOGGER.info("Attempting to delete existing Stripe Customer object for for user:=> {}", userRecord.getEmbeddedContactInfo().getEmail());

        // Initialize Stripe Client API before invoking call
        Stripe.apiKey = stripeAPIConfig.getApiKey();

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

}

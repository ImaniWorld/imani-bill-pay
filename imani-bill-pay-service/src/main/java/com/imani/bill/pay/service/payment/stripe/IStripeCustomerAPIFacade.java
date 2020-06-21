package com.imani.bill.pay.service.payment.stripe;

import com.imani.bill.pay.domain.user.UserRecord;
import com.stripe.model.BankAccount;
import com.stripe.model.Customer;

import java.util.Map;
import java.util.Optional;

/**
 * Interface that defines key methods as facde to be executed for Stripe API's
 *
 * @author manyce400
 */
public interface IStripeCustomerAPIFacade {

    public Optional<Customer> createStripeCustomer(UserRecord userRecord);

    public Optional<Customer> retrieveStripeCustomer(UserRecord userRecord);

    public Optional<BankAccount> createBankAccount(Customer customer, Map<String, Object> params);

    public boolean deleteStripeCustomer(UserRecord userRecord);

}

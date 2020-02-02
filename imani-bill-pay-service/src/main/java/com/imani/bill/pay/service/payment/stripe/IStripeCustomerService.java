package com.imani.bill.pay.service.payment.stripe;

import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.user.UserRecord;
import com.stripe.model.Customer;

import java.util.Optional;

/**
 * Defines interface for creating Stripe Customer.  Stripe Customer is the actual platform UserRecord that will be making platform
 * payments.
 *
 * @author manyce400
 */
public interface IStripeCustomerService {


    public Optional<Customer> createStripeCustomer(UserRecord userRecord);

    public Optional<Customer> retrieveStripeCustomer(UserRecord userRecord);

    public boolean deleteStripeCustomer(UserRecord userRecord);

    public ExecutionResult updatePrimaryStripeCustomerBankAcct(UserRecord userRecord);

    public ExecutionResult createPlaidStripeCustomerBankAcct(UserRecord userRecord);

}

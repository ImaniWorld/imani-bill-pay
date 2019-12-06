package com.imani.bill.pay.service.payment.stripe;

import com.imani.bill.pay.domain.user.UserRecord;
import com.stripe.model.BankAccount;
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

    public Optional<BankAccount> createStripeCustomerBankAcct(UserRecord userRecord, String plaidPublicToken, String plaidAccountID);

}

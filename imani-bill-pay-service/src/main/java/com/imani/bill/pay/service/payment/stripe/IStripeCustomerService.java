package com.imani.bill.pay.service.payment.stripe;

import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.user.UserRecord;

/**
 * Defines interface for creating Stripe Customer.  Stripe Customer is the actual platform UserRecord that will be making platform
 * payments.
 *
 * @author manyce400
 */
public interface IStripeCustomerService {

    public ExecutionResult updatePrimaryStripeCustomerBankAcct(UserRecord userRecord);

    public ExecutionResult createPlaidStripeCustomerBankAcct(UserRecord userRecord);

}

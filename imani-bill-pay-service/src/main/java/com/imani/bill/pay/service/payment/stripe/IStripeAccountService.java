package com.imani.bill.pay.service.payment.stripe;

import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.property.PropertyManager;
import com.imani.bill.pay.domain.property.PropertyOwner;

/**
 * @author manyce400
 */
public interface IStripeAccountService {


    public ExecutionResult createCustomStripeAccount(PropertyOwner propertyOwner);

    public ExecutionResult createCustomStripeAccount(PropertyManager propertyManager);

}

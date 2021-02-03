package com.imani.bill.pay.service.payment.stripe;

import com.imani.bill.pay.domain.business.Business;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.property.PropertyManager;
import com.imani.bill.pay.domain.property.PropertyOwner;
import com.stripe.model.Account;

import java.util.Optional;

/**
 * @author manyce400
 */
public interface IStripeAccountService {


    public Optional<Account> createConnectedStripeAcct(Business business);

    public Optional<Account> getConnectedStripeAcct(Business business);

    public boolean removeConnectedStripeAcct(Business business);

    public ExecutionResult createCustomStripeAccount(PropertyOwner propertyOwner);

    public ExecutionResult createCustomStripeAccount(PropertyManager propertyManager);

}

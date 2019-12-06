package com.imani.bill.pay.service.payment.stripe;

import com.imani.bill.pay.domain.payment.ACHPaymentInfo;
import com.imani.bill.pay.domain.payment.PlaidBankAccount;
import com.imani.bill.pay.domain.property.PropertyManager;
import com.imani.bill.pay.domain.property.PropertyOwner;

import java.util.Optional;

/**
 * @author manyce400
 */
public interface IStripeAccountService {


    public Optional<ACHPaymentInfo> createCustomStripeAccount(PropertyOwner propertyOwner, PlaidBankAccount plaidBankAccount);

    public Optional<ACHPaymentInfo> createCustomStripeAccount(PropertyManager propertyManager, PlaidBankAccount plaidBankAccount);

    public boolean deleteCustomStripeAccount(ACHPaymentInfo achPaymentInfo);
}

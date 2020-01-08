package com.imani.bill.pay.service.payment.stripe;

import com.imani.bill.pay.domain.payment.ACHPaymentInfo;
import com.imani.bill.pay.domain.payment.plaid.PlaidBankAcct;
import com.imani.bill.pay.domain.property.PropertyManager;
import com.imani.bill.pay.domain.property.PropertyOwner;

import java.util.Optional;

/**
 * @author manyce400
 */
public interface IStripeAccountService {


    public Optional<ACHPaymentInfo> createCustomStripeAccount(PropertyOwner propertyOwner, PlaidBankAcct plaidBankAcct);

    public Optional<ACHPaymentInfo> createCustomStripeAccount(PropertyManager propertyManager, PlaidBankAcct plaidBankAcct);

    public boolean deleteCustomStripeAccount(ACHPaymentInfo achPaymentInfo);
}

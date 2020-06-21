package com.imani.bill.pay.service.payment;

import com.imani.bill.pay.domain.payment.ACHPaymentInfo;
import com.imani.bill.pay.domain.payment.IHasPaymentInfo;
import com.imani.bill.pay.domain.payment.plaid.PlaidBankAcct;
import com.imani.bill.pay.domain.property.PropertyManager;
import com.imani.bill.pay.domain.user.UserRecord;
import com.stripe.model.BankAccount;

/**
 * @author manyce400
 */
public interface IACHPaymentInfoService {


    public ACHPaymentInfo findPrimaryPamentInfo(UserRecord userRecord);

    public ACHPaymentInfo findPrimaryPamentInfo(PropertyManager propertyManager);

    public ACHPaymentInfo buildPrimaryACHPaymentInfo(IHasPaymentInfo iHasPaymentInfo);

    public void updateStripeBankAcct(BankAccount stripeBankAccount, ACHPaymentInfo achPaymentInfo);

    public void updateAndSavePlaidBankAcct(PlaidBankAcct plaidBankAcct, ACHPaymentInfo achPaymentInfo);

    public void saveACHPaymentInfo(ACHPaymentInfo achPaymentInfo);

}

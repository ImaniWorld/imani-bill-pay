package com.imani.bill.pay.service.mock;

import com.imani.bill.pay.domain.payment.ACHPaymentInfo;
import com.imani.bill.pay.domain.payment.stripe.StripeBankAcct;
import com.imani.bill.pay.domain.user.UserRecord;

/**
 * @author manyce400
 */
public interface IMockACHPaymentInfoTestBuilder {

    public default ACHPaymentInfo buildACHPaymentInfo(UserRecord userRecord) {
        ACHPaymentInfo achPaymentInfo = ACHPaymentInfo.builder()
                .isPrimary(true)
                .userRecord(userRecord)
                .build();
        return achPaymentInfo;
    }

    public default ACHPaymentInfo buildACHPaymentInfo(UserRecord userRecord, String stripeBankAcctToken) {
        StripeBankAcct stripeBankAcct = StripeBankAcct.builder()
                .bankAcctToken(stripeBankAcctToken)
                .build();

        ACHPaymentInfo achPaymentInfo = ACHPaymentInfo.builder()
                .isPrimary(true)
                .userRecord(userRecord)
                .stripeBankAcct(stripeBankAcct)
                .build();
        return achPaymentInfo;
    }

}

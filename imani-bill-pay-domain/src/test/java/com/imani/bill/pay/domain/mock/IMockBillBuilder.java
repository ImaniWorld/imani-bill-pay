package com.imani.bill.pay.domain.mock;

import com.imani.bill.pay.domain.billing.ImaniBill;

public interface IMockBillBuilder {


    public default ImaniBill build() {
        ImaniBill imaniBill = ImaniBill.builder()
                .amountOwed(100d)
                .build();
        return imaniBill;
    }
}

package com.imani.bill.pay.domain.mock;

import com.imani.bill.pay.domain.billing.BillPayFee;
import com.imani.bill.pay.domain.billing.FeePaymentChargeTypeE;
import com.imani.bill.pay.domain.billing.FeeTypeE;

/**
 * @author manyce400
 */
public interface IMockBillPayFee {

    public default BillPayFee buildLate() {
        BillPayFee billPayFee = BillPayFee.builder()
                .feeName("Mock LateFee")
                .feeTypeE(FeeTypeE.LATE_FEE)
                .feePaymentChargeTypeE(FeePaymentChargeTypeE.FLAT_AMOUNT_FEE)
                .optionalFlatAmount(15d)
                .build();
        return billPayFee;
    }

}

package com.imani.bill.pay.service.billing.fee;

/**
 * Defines functionality for applying fees to Imani BillPay bills.
 *
 * @author manyce400
 */
public interface IBillFeeChargeService<O> {

    // Attempts to charge late fees on all ImaniBill's associated with given WaterServiceAgreement
    public void chargeWaterBillLateFees(O lateFeeTarget);

}

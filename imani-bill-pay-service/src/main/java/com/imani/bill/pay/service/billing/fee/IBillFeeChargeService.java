package com.imani.bill.pay.service.billing.fee;

import com.imani.bill.pay.domain.utility.WaterServiceAgreement;

/**
 * Defines functionality for applying fees to Imani BillPay bills.
 *
 * @author manyce400
 */
public interface IBillFeeChargeService {

    // Attempts to charge late fees on all ImaniBill's associated with the WaterServiceAgreement
    public void chargeWaterBillLateFees(WaterServiceAgreement waterServiceAgreement);


}

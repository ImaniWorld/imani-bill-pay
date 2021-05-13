package com.imani.bill.pay.service.billing.compute;

import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.utility.WaterServiceAgreement;

/**
 * @author manyce400
 */
public interface IBillingFeeComputeService {

    // Based on the amount owed on bill, compute all scheduled fees configured in(AgreementToScheduleBillPayFee) levied against agreement
    public void computeUpdateAmountOwedWithSchedFees(WaterServiceAgreement waterServiceAgreement, ImaniBill imaniBill);

    // Based on the amount owed on bill, compute new amount owed with configured late fee for this agreement
    public void computeUpdateAmountOwedWithLateFee(WaterServiceAgreement waterServiceAgreement, ImaniBill imaniBill);
}

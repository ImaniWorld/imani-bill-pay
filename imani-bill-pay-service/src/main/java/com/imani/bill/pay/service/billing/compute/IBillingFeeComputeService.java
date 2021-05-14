package com.imani.bill.pay.service.billing.compute;

import com.imani.bill.pay.domain.agreement.EmbeddedAgreement;
import com.imani.bill.pay.domain.billing.BillPayFee;
import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.utility.EmbeddedUtilityService;

import java.util.List;

/**
 * @author manyce400
 */
public interface IBillingFeeComputeService {

    // Based on the amount owed on bill, compute all scheduled fees configured in(AgreementToScheduleBillPayFee) levied against agreement
    public void computeUpdateAmountOwedWithSchedFees(List<BillPayFee> billPayFees, ImaniBill imaniBill);

    // Based on the amount owed on bill, compute new amount owed with configured late fee for this agreement
    public void computeUpdateAmountOwedWithLateFee(EmbeddedAgreement embeddedAgreement, EmbeddedUtilityService embeddedUtilityService, ImaniBill imaniBill);

}

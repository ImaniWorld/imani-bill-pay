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

    // Checks to see if late fee has been levied in the current quarter
    public boolean hasLateFeeBeenLeviedInCurrQtr(ImaniBill imaniBill);

    // Checks to see if late fee was levied in the quarter that bill due date was scheduled for
    public boolean hasLateFeeBeenLeviedInBillSchedQtr(ImaniBill imaniBill);

}

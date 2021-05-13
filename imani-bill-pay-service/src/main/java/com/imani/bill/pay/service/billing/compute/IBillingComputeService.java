package com.imani.bill.pay.service.billing.compute;

import com.imani.bill.pay.domain.billing.ImaniBill;

/**
 * Defines functionality for computing a final ImaniBill for Water service agreement
 *
 * @author manyce400
 */
public interface IBillingComputeService<O> {

    // Computes and updates charges on all unpaid Imani bills on agreement.
    public void computeUpdateAgreementBills(O agreement);

    // Computes and update charges on the ImaniBill passed as argument
    public void computeUpdateBill(O agreement, ImaniBill imaniBill);

}

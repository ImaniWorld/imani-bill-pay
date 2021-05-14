package com.imani.bill.pay.service.billing.compute;

import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.utility.SewerServiceAgreement;
import com.imani.bill.pay.domain.utility.WaterServiceAgreement;

/**
 * Defines functionality for computing a final ImaniBill for Water service agreement
 *
 * @author manyce400
 */
public interface IBillingComputeService {

    public void computeUpdateAgreementBills(WaterServiceAgreement waterServiceAgreement);

    public void computeUpdateAgreementBills(SewerServiceAgreement sewerServiceAgreement);

    // Computes and update charges on the ImaniBill passed as argument
    public void computeUpdateBill(WaterServiceAgreement waterServiceAgreement, ImaniBill imaniBill);

    public void computeUpdateBill(SewerServiceAgreement sewerServiceAgreement, ImaniBill imaniBill);

}

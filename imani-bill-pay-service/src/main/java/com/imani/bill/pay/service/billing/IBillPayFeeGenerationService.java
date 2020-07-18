package com.imani.bill.pay.service.billing;

import com.imani.bill.pay.domain.billing.BillPayFeeExplained;
import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.leasemanagement.PropertyLeaseAgreement;
import com.imani.bill.pay.domain.user.UserRecord;

import java.util.List;
import java.util.Optional;

/**
 * @author manyce400
 */
public interface IBillPayFeeGenerationService {

    // Implementation should generate any fees where applicable and apply to Imani Bill.
    public void addImaniBillFees(UserRecord userRecord, PropertyLeaseAgreement propertyLeaseAgreement, ImaniBill imaniBill);

    // Given an ImaniBill implementation will get all current fees applied and will provide an automated explanation
    public Optional<List<BillPayFeeExplained>> explainImaniBillFees(UserRecord userRecord, PropertyLeaseAgreement propertyLeaseAgreement, ImaniBill imaniBill);



}

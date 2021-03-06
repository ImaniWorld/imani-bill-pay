package com.imani.bill.pay.service.mock;

import com.imani.bill.pay.domain.property.LeaseAgreement;
import org.joda.time.DateTime;

/**
 * @author manyce400
 */
public interface IMockRentalAgreementTestBuilder {


    public default LeaseAgreement buildRentalAgreement() {
        LeaseAgreement leaseAgreement = LeaseAgreement.builder()
                .propertyManagerAcceptedAgreement(true)
                .propertyOwnerAcceptedAgreement(true)
                .tenantAcceptedAgreement(true)
                .agreementInEffect(true)
                .monthlyRentalCost(1800.00)
                .effectiveDate(DateTime.now())
                .build();
        return leaseAgreement;
    }

}

package com.imani.bill.pay.service.property.lease;

import com.imani.bill.pay.domain.property.LeaseAgreementTypeE;
import com.imani.bill.pay.domain.property.Property;
import com.imani.bill.pay.domain.user.UserRecord;

/**
 * @author manyce400
 */
public interface IPropertyLeaseService {

    public void leaseProperty(UserRecord userRecord, Property property, Double monthlyRentalCost, LeaseAgreementTypeE leaseAgreementTypeE);

}

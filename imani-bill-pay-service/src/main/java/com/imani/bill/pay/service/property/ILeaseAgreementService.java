package com.imani.bill.pay.service.property;

import com.imani.bill.pay.domain.property.Apartment;
import com.imani.bill.pay.domain.property.LeaseAgreement;
import com.imani.bill.pay.domain.property.LeaseAgreementTypeE;
import com.imani.bill.pay.domain.property.PropertyManager;
import com.imani.bill.pay.domain.user.UserRecord;

/**
 * @author manyce400
 */
public interface ILeaseAgreementService {

    public LeaseAgreement buildLeaseAgreement(UserRecord userRecord, Apartment apartment, PropertyManager propertyManager, Double monthlyRentalCost, LeaseAgreementTypeE leaseAgreementTypeE);

    public boolean isLeaseAgreementInForce(LeaseAgreement leaseAgreement);

}
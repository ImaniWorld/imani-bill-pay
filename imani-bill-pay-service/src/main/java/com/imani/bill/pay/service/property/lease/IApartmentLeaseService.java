package com.imani.bill.pay.service.property.lease;

import com.imani.bill.pay.domain.property.Apartment;
import com.imani.bill.pay.domain.property.LeaseAgreement;
import com.imani.bill.pay.domain.property.LeaseAgreementTypeE;
import com.imani.bill.pay.domain.property.PropertyManager;
import com.imani.bill.pay.domain.user.UserRecord;

import java.util.Optional;

/**
 * @author manyce400
 */
public interface IApartmentLeaseService {

    public Optional<LeaseAgreement> leaseApartment(UserRecord userRecord, Apartment apartment, PropertyManager propertyManager, Double monthlyRentalCost, LeaseAgreementTypeE leaseAgreementTypeE);
}

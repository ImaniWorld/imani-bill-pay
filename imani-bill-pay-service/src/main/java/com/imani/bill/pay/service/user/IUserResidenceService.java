package com.imani.bill.pay.service.user;

import com.imani.bill.pay.domain.property.Apartment;
import com.imani.bill.pay.domain.property.LeaseAgreement;
import com.imani.bill.pay.domain.property.Property;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.UserResidence;

/**
 * @author manyce400
 */
public interface IUserResidenceService {


    // Creates UserResidence for a User with the expectation that this Property is a Single Family property.
    public UserResidence buildUserResidence(UserRecord userRecord, Property property, boolean primaryResidence);

    // Creates UserResidence for a User with the expectation that this Property is a Single Family property with a rental agreement
    public UserResidence buildUserResidence(UserRecord userRecord, Property property, LeaseAgreement leaseAgreement, boolean primaryResidence);

    // Creates UserResidence for a User in an apartment
    public UserResidence buildUserResidence(UserRecord userRecord, Apartment apartment, boolean primaryResidence);

    // Creates UserResidence for a User in an  apartment with a lease agreement
    public UserResidence buildUserResidence(UserRecord userRecord, Apartment apartment, LeaseAgreement leaseAgreement, boolean primaryResidence);

}

package com.imani.bill.pay.service.user;

import com.imani.bill.pay.domain.property.Apartment;
import com.imani.bill.pay.domain.property.Property;
import com.imani.bill.pay.domain.property.RentalAgreement;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.UserResidence;

/**
 * @author manyce400
 */
public interface IUserResidenceService {


    // Creates UserResidence for a User with the expectation that this Property is a Single Family property.
    public UserResidence buildUserResidence(UserRecord userRecord, Property property, boolean primaryResidence);

    // Creates UserResidence for a User with the expectation that this Property is a Single Family property with a rental agreement
    public UserResidence buildUserResidence(UserRecord userRecord, Property property, RentalAgreement rentalAgreement, boolean primaryResidence);

    // Creates UserResidence for a User with the expectation that this Property is a Multi Family property.
    public UserResidence buildUserResidence(UserRecord userRecord, Property property, Apartment apartment, boolean primaryResidence);

    // Creates UserResidence for a User with the expectation that this Property is a Multi Family property with a rental agreement
    public UserResidence buildUserResidence(UserRecord userRecord, Property property, Apartment apartment, RentalAgreement rentalAgreement, boolean primaryResidence);

}

package com.imani.bill.pay.service.property.lease;

import com.imani.bill.pay.domain.property.Apartment;
import com.imani.bill.pay.domain.property.LeaseAgreement;
import com.imani.bill.pay.domain.property.LeaseAgreementTypeE;
import com.imani.bill.pay.domain.property.PropertyManager;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.UserResidence;
import com.imani.bill.pay.service.property.ILeaseAgreementService;
import com.imani.bill.pay.service.property.LeaseAgreementService;
import com.imani.bill.pay.service.user.IUserResidenceService;
import com.imani.bill.pay.service.user.UserResidenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * @author manyce400
 */
@Service(ApartmentLeaseService.SPRING_BEAN)
public class ApartmentLeaseService implements IApartmentLeaseService {


    @Autowired
    @Qualifier(LeaseAgreementService.SPRING_BEAN)
    private ILeaseAgreementService iLeaseAgreementService;

    @Autowired
    @Qualifier(UserResidenceService.SPRING_BEAN)
    private IUserResidenceService iUserResidenceService;

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.property.lease.ApartmentLeaseService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PropertyLeaseService.class);


    @Transactional
    @Override
    public Optional<LeaseAgreement> leaseApartment(UserRecord userRecord, Apartment apartment, PropertyManager propertyManager, Double monthlyRentalCost, LeaseAgreementTypeE leaseAgreementTypeE) {
        Assert.notNull(userRecord, "userRecord cannot be null");
        Assert.notNull(apartment, "apartment cannot be null");
        Assert.notNull(propertyManager, "propertyManager cannot be null");
        Assert.notNull(monthlyRentalCost, "monthlyRentalCost cannot be null");
        Assert.notNull(leaseAgreementTypeE, "leaseAgreementTypeE cannot be null");

        LOGGER.info("Leasing apartment => {} to user => {}", apartment, userRecord.getEmbeddedContactInfo().getEmail());

        // Verify that this Apartment is not already leased
        LeaseAgreement existingLeaseAgreement = iLeaseAgreementService.findApartmentLeaseAgreement(apartment);
        if(existingLeaseAgreement == null) {
            // Create a LeaseAgreement to reflect this transation.
            LeaseAgreement leaseAgreement = iLeaseAgreementService.buildLeaseAgreement(userRecord, apartment, propertyManager, monthlyRentalCost, leaseAgreementTypeE);

            // Record the UserResidence to be this new apartment
            UserResidence userResidence = iUserResidenceService.buildUserResidence(userRecord, apartment, leaseAgreement, true);
            return Optional.of(leaseAgreement);
        }

        LOGGER.info("Apartment is currently leased already with existingLeaseAgreement:=> {}", existingLeaseAgreement);
        return Optional.empty();
    }
}

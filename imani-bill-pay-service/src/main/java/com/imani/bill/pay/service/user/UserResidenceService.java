package com.imani.bill.pay.service.user;

import com.imani.bill.pay.domain.property.Apartment;
import com.imani.bill.pay.domain.property.LeaseAgreement;
import com.imani.bill.pay.domain.property.Property;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.UserResidence;
import com.imani.bill.pay.domain.user.repository.IUserResidenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

/**
 * @author manyce400
 */
@Service(UserResidenceService.SPRING_BEAN)
public class UserResidenceService implements IUserResidenceService {



    @Autowired
    private IUserResidenceRepository iUserResidenceRepository;


    public static final String SPRING_BEAN = "com.imani.bill.pay.service.user.UserResidenceService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(UserResidenceService.class);


    @Transactional
    @Override
    public UserResidence buildUserResidence(UserRecord userRecord, Property property, boolean primaryResidence) {
        Assert.notNull(userRecord, "UserRecord cannot be null");
        Assert.notNull(property, "Property cannot be null");
        Assert.notNull(property.getPropertyTypeE(), "Property Type cannot be null");

        LOGGER.debug("Building residence for user:=> {}", userRecord.getEmbeddedContactInfo().getEmail());

        UserResidence userResidence =  UserResidence.builder()
                .userRecord(userRecord)
                .property(property)
                .primaryResidence(primaryResidence)
                .build();

        iUserResidenceRepository.save(userResidence);
        return userResidence;
    }

    @Transactional
    @Override
    public UserResidence buildUserResidence(UserRecord userRecord, Property property, LeaseAgreement leaseAgreement, boolean primaryResidence) {
        Assert.notNull(userRecord, "UserRecord cannot be null");
        Assert.notNull(property, "Property cannot be null");
        Assert.notNull(leaseAgreement, "leaseAgreement cannot be null");
        Assert.notNull(property.getPropertyTypeE(), "Property Type cannot be null");

        LOGGER.debug("Building residence for user:=> {} with rental agreement", userRecord.getEmbeddedContactInfo().getEmail());

        UserResidence userResidence =  UserResidence.builder()
                .userRecord(userRecord)
                .property(property)
                .rentalAgreement(leaseAgreement)
                .primaryResidence(primaryResidence)
                .build();

        iUserResidenceRepository.save(userResidence);
        return userResidence;
    }

    @Transactional
    @Override
    public UserResidence buildUserResidence(UserRecord userRecord, Apartment apartment, boolean primaryResidence) {
        Assert.notNull(userRecord, "UserRecord cannot be null");
        Assert.notNull(apartment, "apartment cannot be null");
        Assert.notNull(apartment.getFloor(), "Apartment Floor cannot be null");

        LOGGER.debug("Building residence for user:=> {}", userRecord.getEmbeddedContactInfo().getEmail());

        UserResidence userResidence =  UserResidence.builder()
                .userRecord(userRecord)
                .apartment(apartment)
                .apartment(apartment)
                .primaryResidence(primaryResidence)
                .build();

        iUserResidenceRepository.save(userResidence);
        return userResidence;
    }

    @Transactional
    @Override
    public UserResidence buildUserResidence(UserRecord userRecord, Apartment apartment, LeaseAgreement leaseAgreement, boolean primaryResidence) {
        Assert.notNull(userRecord, "UserRecord cannot be null");
        Assert.notNull(apartment, "apartment cannot be null");
        Assert.notNull(leaseAgreement, "leaseAgreement cannot be null");
        Assert.notNull(apartment.getFloor(), "Apartment Floor cannot be null");

        LOGGER.debug("Building residence for user:=> {}", userRecord.getEmbeddedContactInfo().getEmail());

        UserResidence userResidence =  UserResidence.builder()
                .userRecord(userRecord)
                .apartment(apartment)
                .rentalAgreement(leaseAgreement)
                .primaryResidence(primaryResidence)
                .build();

        iUserResidenceRepository.save(userResidence);
        return userResidence;
    }

}

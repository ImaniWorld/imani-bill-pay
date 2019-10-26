package com.imani.bill.pay.service.user;

import com.imani.bill.pay.domain.property.Apartment;
import com.imani.bill.pay.domain.property.Property;
import com.imani.bill.pay.domain.property.RentalAgreement;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.UserResidence;
import com.imani.bill.pay.domain.user.repository.IUserResidenceRepository;
import com.imani.bill.pay.service.mock.IMockApartmentTestBuilder;
import com.imani.bill.pay.service.mock.IMockPropertyTestBuilder;
import com.imani.bill.pay.service.mock.IMockRentalAgreementTestBuilder;
import com.imani.bill.pay.service.mock.IMockUserRecordTestBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author manyce400
 */
@RunWith(MockitoJUnitRunner.class)
public class UserResidenceServiceTest implements IMockUserRecordTestBuilder, IMockPropertyTestBuilder, IMockRentalAgreementTestBuilder, IMockApartmentTestBuilder {


    @Mock
    private IUserResidenceRepository iUserResidenceRepository;

    @InjectMocks
    private UserResidenceService userResidenceService;


    @Test
    public void testBuildUserResidenceMinimum() {
        UserRecord userRecord = buildUserRecord();
        Property property = buildMultiFamilyProperty();

        UserResidence userResidence = userResidenceService.buildUserResidence(userRecord, property, true);

        // Verify that save is called at least once
        Mockito.verify(iUserResidenceRepository, Mockito.times(1)).save(Mockito.any());

        // verify results
        Assert.assertNotNull(userResidence);
        Assert.assertTrue(userResidence.isPrimaryResidence());
        Assert.assertNull(userResidence.getApartment());
        Assert.assertNull(userResidence.getRentalAgreement());
        Assert.assertEquals(userRecord, userResidence.getUserRecord());
        Assert.assertEquals(property, userResidence.getProperty());
    }

    @Test
    public void testBuildUserResidenceRentalAgreement() {
        UserRecord userRecord = buildUserRecord();
        Property property = buildMultiFamilyProperty();
        RentalAgreement rentalAgreement = buildRentalAgreement();

        UserResidence userResidence = userResidenceService.buildUserResidence(userRecord, property, rentalAgreement, true);

        // Verify that save is called at least once
        Mockito.verify(iUserResidenceRepository, Mockito.times(1)).save(Mockito.any());

        // verify results
        Assert.assertNotNull(userResidence);
        Assert.assertTrue(userResidence.isPrimaryResidence());
        Assert.assertEquals(userRecord, userResidence.getUserRecord());
        Assert.assertEquals(rentalAgreement, userResidence.getRentalAgreement());
        Assert.assertEquals(property, userResidence.getProperty());
    }


    @Test
    public void testBuildUserResidencePropertyAndApartment() {
        UserRecord userRecord = buildUserRecord();
        Property property = buildMultiFamilyProperty();
        Apartment apartment = buildApartment();

        UserResidence userResidence = userResidenceService.buildUserResidence(userRecord, property, apartment, true);

        // Verify that save is called at least once
        Mockito.verify(iUserResidenceRepository, Mockito.times(1)).save(Mockito.any());

        // verify results
        Assert.assertNotNull(userResidence);
        Assert.assertTrue(userResidence.isPrimaryResidence());
        Assert.assertNull(userResidence.getRentalAgreement());
        Assert.assertEquals(userRecord, userResidence.getUserRecord());
        Assert.assertEquals(property, userResidence.getProperty());
        Assert.assertEquals(apartment, userResidence.getApartment());
    }

    @Test
    public void testBuildUserResidencePropertyApartmentAndAgreement() {
        UserRecord userRecord = buildUserRecord();
        Property property = buildMultiFamilyProperty();
        Apartment apartment = buildApartment();
        RentalAgreement rentalAgreement = buildRentalAgreement();

        UserResidence userResidence = userResidenceService.buildUserResidence(userRecord, property, apartment, rentalAgreement, true);

        // Verify that save is called at least once
        Mockito.verify(iUserResidenceRepository, Mockito.times(1)).save(Mockito.any());

        // verify results
        Assert.assertNotNull(userResidence);
        Assert.assertTrue(userResidence.isPrimaryResidence());
        Assert.assertEquals(userRecord, userResidence.getUserRecord());
        Assert.assertEquals(property, userResidence.getProperty());
        Assert.assertEquals(apartment, userResidence.getApartment());
        Assert.assertEquals(rentalAgreement, userResidence.getRentalAgreement());
    }

}
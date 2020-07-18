package com.imani.bill.pay.service.user;

import com.imani.bill.pay.domain.property.Apartment;
import com.imani.bill.pay.domain.property.LeaseAgreement;
import com.imani.bill.pay.domain.property.Property;
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
//        UserRecord userRecord = buildUserRecord();
//        Property property = buildMultiFamilyProperty();
//
//        UserResidence userResidence = userResidenceService.buildUserResidence(userRecord, property, true);
//
//        // Verify that save is called at least once
//        Mockito.verify(iUserResidenceRepository, Mockito.times(1)).save(Mockito.any());
//
//        // verify results
//        Assert.assertNotNull(userResidence);
//        Assert.assertTrue(userResidence.isPrimaryResidence());
//        Assert.assertNull(userResidence.getApartment());
////        Assert.assertNull(userResidence.getLeaseAgreement());
//        Assert.assertEquals(userRecord, userResidence.getUserRecord());
//        Assert.assertEquals(property, userResidence.getProperty());
    }

//    @Test
    public void testBuildUserResidenceRentalAgreement() {
        UserRecord userRecord = buildUserRecord();
        Property property = buildMultiFamilyProperty();
        LeaseAgreement leaseAgreement = buildRentalAgreement();

        UserResidence userResidence = userResidenceService.buildUserResidence(userRecord, property, leaseAgreement, true);

        // Verify that save is called at least once
        Mockito.verify(iUserResidenceRepository, Mockito.times(1)).save(Mockito.any());

        // verify results
        Assert.assertNotNull(userResidence);
        Assert.assertTrue(userResidence.isPrimaryResidence());
        Assert.assertEquals(userRecord, userResidence.getUserRecord());
//        Assert.assertEquals(leaseAgreement, userResidence.getLeaseAgreement());
        Assert.assertEquals(property, userResidence.getProperty());
    }


//    @Test
    public void testBuildUserResidencePropertyAndApartment() {
        UserRecord userRecord = buildUserRecord();
        Apartment apartment = buildApartment();

        UserResidence userResidence = userResidenceService.buildUserResidence(userRecord, apartment, true);

        // Verify that save is called at least once
        Mockito.verify(iUserResidenceRepository, Mockito.times(1)).save(Mockito.any());

        // verify results
        Assert.assertNotNull(userResidence);
        Assert.assertTrue(userResidence.isPrimaryResidence());
//        Assert.assertNull(userResidence.getLeaseAgreement());
        Assert.assertEquals(userRecord, userResidence.getUserRecord());
        Assert.assertEquals(apartment, userResidence.getApartment());
    }

//    @Test
    public void testBuildUserResidencePropertyApartmentAndAgreement() {
        UserRecord userRecord = buildUserRecord();
        Apartment apartment = buildApartment();
        LeaseAgreement leaseAgreement = buildRentalAgreement();

        UserResidence userResidence = userResidenceService.buildUserResidence(userRecord, apartment, leaseAgreement, true);

        // Verify that save is called at least once
        Mockito.verify(iUserResidenceRepository, Mockito.times(1)).save(Mockito.any());

        // verify results
        Assert.assertNotNull(userResidence);
        Assert.assertTrue(userResidence.isPrimaryResidence());
        Assert.assertEquals(userRecord, userResidence.getUserRecord());
        Assert.assertEquals(apartment, userResidence.getApartment());
//        Assert.assertEquals(leaseAgreement, userResidence.getLeaseAgreement());
    }

}
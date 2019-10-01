package com.imani.bill.pay.service.property;

import com.imani.bill.pay.domain.property.RentalAgreement;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author manyce400
 */
@RunWith(MockitoJUnitRunner.class)
public class RentalAgreementServiceTest {


    @InjectMocks
    private RentalAgreementService rentalAgreementService;


    @Test
    public void testAgreementHasDocument() {
        RentalAgreement rentalAgreement = RentalAgreement.builder()
                .agreementDocument("C://///")
                .tenantAcceptedAgreement(true)
                .propertyManagerAcceptedAgreement(true)
                .build();
        Assert.assertTrue(rentalAgreementService.agreementHasDocument(rentalAgreement));

        rentalAgreement = RentalAgreement.builder()
                .tenantAcceptedAgreement(true)
                .propertyManagerAcceptedAgreement(true)
                .build();
        Assert.assertFalse(rentalAgreementService.agreementHasDocument(rentalAgreement));
    }

    @Test
    public void testAgreementHasEffectiveDate() {
        RentalAgreement rentalAgreement = RentalAgreement.builder()
                .tenantAcceptedAgreement(true)
                .propertyManagerAcceptedAgreement(true)
                .build();
        Assert.assertFalse(rentalAgreementService.agreementHasEffectiveDate(rentalAgreement));

        rentalAgreement = RentalAgreement.builder()
                .effectiveDate(DateTime.now())
                .tenantAcceptedAgreement(true)
                .propertyManagerAcceptedAgreement(true)
                .build();
        Assert.assertTrue(rentalAgreementService.agreementHasEffectiveDate(rentalAgreement));
    }

    @Test
    public void testPartiesAcceptedAgreement() {
        RentalAgreement rentalAgreement = RentalAgreement.builder()
                .tenantAcceptedAgreement(true)
                .propertyManagerAcceptedAgreement(true)
                .build();
        Assert.assertTrue(rentalAgreementService.partiesAcceptedAgreement(rentalAgreement));

        rentalAgreement = RentalAgreement.builder()
                .tenantAcceptedAgreement(false)
                .propertyManagerAcceptedAgreement(true)
                .build();
        Assert.assertFalse(rentalAgreementService.partiesAcceptedAgreement(rentalAgreement));
    }

    @Test
    public void testIsRentalAgreementInForce() {
        RentalAgreement rentalAgreement = RentalAgreement.builder()
                .tenantAcceptedAgreement(true)
                .propertyManagerAcceptedAgreement(true)
                .build();
        Assert.assertFalse(rentalAgreementService.isRentalAgreementInForce(rentalAgreement));

        rentalAgreement = RentalAgreement.builder()
                .agreementDocument("C://///")
                .effectiveDate(DateTime.now())
                .propertyManagerAcceptanceDate(DateTime.now())
                .tenantAcceptanceDate(DateTime.now())
                .tenantAcceptedAgreement(true)
                .propertyManagerAcceptedAgreement(true)
                .build();
        Assert.assertTrue(rentalAgreementService.isRentalAgreementInForce(rentalAgreement));

        rentalAgreement = RentalAgreement.builder()
                .agreementDocument("C://///")
                .effectiveDate(DateTime.now())
                .propertyOwnerAcceptedAgreement(true)
                .propertyOwnerAcceptanceDate(DateTime.now())
                .tenantAcceptanceDate(DateTime.now())
                .tenantAcceptedAgreement(true)
                .build();
        Assert.assertTrue(rentalAgreementService.isRentalAgreementInForce(rentalAgreement));
    }


}

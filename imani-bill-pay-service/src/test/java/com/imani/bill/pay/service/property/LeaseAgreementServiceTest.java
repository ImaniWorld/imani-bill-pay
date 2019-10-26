package com.imani.bill.pay.service.property;

import com.imani.bill.pay.domain.property.LeaseAgreement;
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
public class LeaseAgreementServiceTest {


    @InjectMocks
    private LeaseAgreementService leaseAgreementService;


    @Test
    public void testAgreementHasDocument() {
        LeaseAgreement leaseAgreement = LeaseAgreement.builder()
                .agreementDocument("C://///")
                .tenantAcceptedAgreement(true)
                .propertyManagerAcceptedAgreement(true)
                .build();
        Assert.assertTrue(leaseAgreementService.agreementHasDocument(leaseAgreement));

        leaseAgreement = LeaseAgreement.builder()
                .tenantAcceptedAgreement(true)
                .propertyManagerAcceptedAgreement(true)
                .build();
        Assert.assertFalse(leaseAgreementService.agreementHasDocument(leaseAgreement));
    }

    @Test
    public void testAgreementHasEffectiveDate() {
        LeaseAgreement leaseAgreement = LeaseAgreement.builder()
                .tenantAcceptedAgreement(true)
                .propertyManagerAcceptedAgreement(true)
                .build();
        Assert.assertFalse(leaseAgreementService.agreementHasEffectiveDate(leaseAgreement));

        leaseAgreement = LeaseAgreement.builder()
                .effectiveDate(DateTime.now())
                .tenantAcceptedAgreement(true)
                .propertyManagerAcceptedAgreement(true)
                .build();
        Assert.assertTrue(leaseAgreementService.agreementHasEffectiveDate(leaseAgreement));
    }

    @Test
    public void testPartiesAcceptedAgreement() {
        LeaseAgreement leaseAgreement = LeaseAgreement.builder()
                .tenantAcceptedAgreement(true)
                .propertyManagerAcceptedAgreement(true)
                .build();
        Assert.assertTrue(leaseAgreementService.partiesAcceptedAgreement(leaseAgreement));

        leaseAgreement = LeaseAgreement.builder()
                .tenantAcceptedAgreement(false)
                .propertyManagerAcceptedAgreement(true)
                .build();
        Assert.assertFalse(leaseAgreementService.partiesAcceptedAgreement(leaseAgreement));
    }

    @Test
    public void testIsRentalAgreementInForce() {
        LeaseAgreement leaseAgreement = LeaseAgreement.builder()
                .tenantAcceptedAgreement(true)
                .propertyManagerAcceptedAgreement(true)
                .build();
        Assert.assertFalse(leaseAgreementService.isLeaseAgreementInForce(leaseAgreement));

        leaseAgreement = LeaseAgreement.builder()
                .agreementDocument("C://///")
                .effectiveDate(DateTime.now())
                .propertyManagerAcceptanceDate(DateTime.now())
                .tenantAcceptanceDate(DateTime.now())
                .tenantAcceptedAgreement(true)
                .propertyManagerAcceptedAgreement(true)
                .build();
        Assert.assertTrue(leaseAgreementService.isLeaseAgreementInForce(leaseAgreement));

        leaseAgreement = LeaseAgreement.builder()
                .agreementDocument("C://///")
                .effectiveDate(DateTime.now())
                .propertyOwnerAcceptedAgreement(true)
                .propertyOwnerAcceptanceDate(DateTime.now())
                .tenantAcceptanceDate(DateTime.now())
                .tenantAcceptedAgreement(true)
                .build();
        Assert.assertTrue(leaseAgreementService.isLeaseAgreementInForce(leaseAgreement));
    }


}

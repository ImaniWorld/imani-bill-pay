package com.imani.bill.pay.service.property;

import com.imani.bill.pay.domain.property.Apartment;
import com.imani.bill.pay.domain.property.LeaseAgreement;
import com.imani.bill.pay.domain.property.LeaseAgreementTypeE;
import com.imani.bill.pay.domain.property.PropertyManager;
import com.imani.bill.pay.domain.property.repository.ILeaseAgreementRepository;
import com.imani.bill.pay.domain.user.UserRecord;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

/**
 * @author manyce400
 */
@Service(LeaseAgreementService.SPRING_BEAN)
public class LeaseAgreementService implements ILeaseAgreementService {


    @Autowired
    private ILeaseAgreementRepository iLeaseAgreementRepository;

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.property.LeaseAgreementService";


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(LeaseAgreementService.class);


    @Transactional
    @Override
    public LeaseAgreement buildLeaseAgreement(UserRecord userRecord, Apartment apartment, PropertyManager propertyManager, Double monthlyRentalCost, LeaseAgreementTypeE leaseAgreementTypeE) {
        Assert.notNull(userRecord, "userRecord cannot be null");
        Assert.notNull(apartment, "apartment cannot be null");
        Assert.notNull(propertyManager, "propertyManager cannot be null");
        Assert.notNull(monthlyRentalCost, "monthlyRentalCost cannot be null");
        Assert.notNull(leaseAgreementTypeE, "leaseAgreementTypeE cannot be null");

        LOGGER.info("Building in force rental agreement for User:=> {} with PropertyManager: {}", userRecord.getEmbeddedContactInfo().getEmail(), propertyManager.getName());

        LeaseAgreement leaseAgreement = LeaseAgreement.builder()
                .leaseAgreementTypeE(leaseAgreementTypeE)
                .effectiveDate(DateTime.now())
                .agreementInEffect(true)
                .tenantAcceptedAgreement(true)
                .propertyManagerAcceptedAgreement(true)
                .tenantAcceptanceDate(DateTime.now())
                .propertyManagerAcceptanceDate(DateTime.now())
                .monthlyRentalCost(monthlyRentalCost)
                .userRecord(userRecord)
                .apartment(apartment)
                .propertyManager(propertyManager)
                .build();
        iLeaseAgreementRepository.save(leaseAgreement);
        return leaseAgreement;
    }

    @Override
    public boolean isLeaseAgreementInForce(LeaseAgreement leaseAgreement) {
        Assert.notNull(leaseAgreement, "LeaseAgreement cannot be null");
        LOGGER.debug("Checking LeaseAgreement to see if its still in force => {}", leaseAgreement);

        boolean agreementHasDocument = agreementHasDocument(leaseAgreement);
        boolean agreementHasEffectiveDate = agreementHasEffectiveDate(leaseAgreement);

        if(agreementHasEffectiveDate && agreementHasDocument) {
            boolean partiesAcceptedAgreement = partiesAcceptedAgreement(leaseAgreement);
            boolean partiesAcceptanceDatesRecorded = partiesAcceptanceDatesRecorded(leaseAgreement);

            if(partiesAcceptedAgreement && partiesAcceptanceDatesRecorded) {
                return true;
            }
        }

        LOGGER.info("LeaseAgreement with ID: {} is not in force, agreement has no document or effective date", leaseAgreement.getId());
        return false;
    }

    boolean agreementHasDocument(LeaseAgreement leaseAgreement) {
        return leaseAgreement.getAgreementDocument() != null && leaseAgreement.getAgreementDocument().length() > 0;
    }

    boolean agreementHasEffectiveDate(LeaseAgreement leaseAgreement) {
        return leaseAgreement.getEffectiveDate() != null;
    }

    boolean partiesAcceptedAgreement(LeaseAgreement leaseAgreement) {
        // Agreement can only be between a Tenant and a Property manager or a Tenant and a Property Owner
        if(leaseAgreement.isTenantAcceptedAgreement()) {
            return leaseAgreement.isPropertyManagerAcceptedAgreement() || leaseAgreement.isPropertyOwnerAcceptedAgreement();
        }

        return false;
    }

    boolean partiesAcceptanceDatesRecorded(LeaseAgreement leaseAgreement) {
        if(leaseAgreement.getTenantAcceptanceDate() != null) {
            return leaseAgreement.getPropertyManagerAcceptanceDate() != null || leaseAgreement.getPropertyOwnerAcceptanceDate() != null;
        }

        return false;
    }
}

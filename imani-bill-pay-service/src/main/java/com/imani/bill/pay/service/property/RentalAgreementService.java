package com.imani.bill.pay.service.property;

import com.imani.bill.pay.domain.property.RentalAgreement;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author manyce400
 */
@Service(RentalAgreementService.SPRING_BEAN)
public class RentalAgreementService implements IRentalAgreementService {



    public static final String SPRING_BEAN = "com.imani.cash.domain.service.property.rental.RentalAgreementService";


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(RentalAgreementService.class);




    @Override
    public boolean isRentalAgreementInForce(RentalAgreement rentalAgreement) {
        Assert.notNull(rentalAgreement, "RentalAgreement cannot be null");
        LOGGER.debug("Checking RentalAgreement to see if its still in force => {}", rentalAgreement);

        boolean agreementHasDocument = agreementHasDocument(rentalAgreement);
        boolean agreementHasEffectiveDate = agreementHasEffectiveDate(rentalAgreement);

        if(agreementHasEffectiveDate && agreementHasDocument) {
            boolean partiesAcceptedAgreement = partiesAcceptedAgreement(rentalAgreement);
            boolean partiesAcceptanceDatesRecorded = partiesAcceptanceDatesRecorded(rentalAgreement);

            if(partiesAcceptedAgreement && partiesAcceptanceDatesRecorded) {
                return true;
            }
        }

        LOGGER.info("RentalAgreement with ID: {} is not in force, agreement has no document or effective date", rentalAgreement.getId());
        return false;
    }

    boolean agreementHasDocument(RentalAgreement rentalAgreement) {
        return rentalAgreement.getAgreementDocument() != null && rentalAgreement.getAgreementDocument().length() > 0;
    }

    boolean agreementHasEffectiveDate(RentalAgreement rentalAgreement) {
        return rentalAgreement.getEffectiveDate() != null;
    }

    boolean partiesAcceptedAgreement(RentalAgreement rentalAgreement) {
        // Agreement can only be between a Tenant and a Property manager or a Tenant and a Property Owner
        if(rentalAgreement.isTenantAcceptedAgreement()) {
            return rentalAgreement.isPropertyManagerAcceptedAgreement() || rentalAgreement.isPropertyOwnerAcceptedAgreement();
        }

        return false;
    }

    boolean partiesAcceptanceDatesRecorded(RentalAgreement rentalAgreement) {
        if(rentalAgreement.getTenantAcceptanceDate() != null) {
            return rentalAgreement.getPropertyManagerAcceptanceDate() != null || rentalAgreement.getPropertyOwnerAcceptanceDate() != null;
        }

        return false;
    }
}

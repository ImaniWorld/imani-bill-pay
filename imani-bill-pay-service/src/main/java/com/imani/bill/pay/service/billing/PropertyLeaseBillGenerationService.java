package com.imani.bill.pay.service.billing;

import com.imani.bill.pay.domain.billing.BillScheduleTypeE;
import com.imani.bill.pay.domain.billing.BillServiceRenderedTypeE;
import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.billing.ImaniBillExplained;
import com.imani.bill.pay.domain.billing.repository.IImaniBillRepository;
import com.imani.bill.pay.domain.leasemanagement.PropertyLeaseAgreement;
import com.imani.bill.pay.domain.property.Property;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.UserRecordLite;
import com.imani.bill.pay.domain.user.UserResidence;
import com.imani.bill.pay.domain.user.repository.IUserRecordRepository;
import com.imani.bill.pay.domain.user.repository.IUserResidenceRepository;
import com.imani.bill.pay.service.util.DateTimeUtil;
import com.imani.bill.pay.service.util.IDateTimeUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * This implementation generates user lease agreement bills.
 *
 * @author manyce400
 */
@Service(PropertyLeaseBillGenerationService.SPRING_BEAN)
public class PropertyLeaseBillGenerationService implements IBillGenerationService {


    @Autowired
    @Qualifier(DateTimeUtil.SPRING_BEAN)
    private IDateTimeUtil iDateTimeUtil;

    @Autowired
    private IUserRecordRepository iUserRecordRepository;

    @Autowired
    private IImaniBillRepository imaniBillRepository;

    @Autowired
    private IUserResidenceRepository iUserResidenceRepository;

    @Autowired
    @Qualifier(PropertyLeaseFeeGenerationService.SPRING_BEAN)
    private IBillPayFeeGenerationService iBillPayFeeGenerationService;


    public static final String SPRING_BEAN = "com.imani.bill.pay.service.billing.LeaseBillGenerationService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PropertyLeaseBillGenerationService.class);


    @Transactional
    @Override
    public boolean generateImaniBill(UserRecord userRecord) {
        Assert.notNull(userRecord, "UserRecord cannot be null");

        LOGGER.info("Attempting to generate Lease Agreement bill for user:=> {}", userRecord.getEmbeddedContactInfo().getEmail());

        // Lookup user, residence and lease agreement information
        userRecord = iUserRecordRepository.findByUserEmail(userRecord.getEmbeddedContactInfo().getEmail());
        UserResidence userResidence = iUserResidenceRepository.findUserResidence(userRecord);

        if(userResidence != null && userResidence.getPropertyLeaseAgreement() != null) {
            PropertyLeaseAgreement propertyLeaseAgreement = userResidence.getPropertyLeaseAgreement();
            Property property = getLeaseProperty(propertyLeaseAgreement);

            if(propertyLeaseAgreement.getEmbeddedAgreement().isAgreementInForce()) {
                DateTime dateTimeAtStartOfMonth = iDateTimeUtil.getDateTimeAtStartOfMonth(DateTime.now());
                LOGGER.info("User lease agreement found @ property: {}. ", property.getPrintableAddress());

                // Check to see if a lease bill has been generated already for the current month
                ImaniBill imaniBill = imaniBillRepository.getImaniBillForScheduleDate(userRecord, dateTimeAtStartOfMonth);
                if(imaniBill == null) {
                    LOGGER.info("Generating new property-lease-agreement Imani Bill Month: {}", dateTimeAtStartOfMonth.monthOfYear().getAsText());
                    imaniBill = createLeaseImaniBill(userRecord, propertyLeaseAgreement, dateTimeAtStartOfMonth);
                }

                // Apply all required fees on Imani Bill.
                iBillPayFeeGenerationService.addImaniBillFees(userRecord, propertyLeaseAgreement, imaniBill);
                imaniBillRepository.save(imaniBill);
                return true;
            }

        }

        return false;
    }

    @Transactional
    @Override
    public Optional<ImaniBillExplained> genCurrentBillExplanation(UserRecordLite userRecordLite) {
        UserRecord userRecord = iUserRecordRepository.findByUserEmail(userRecordLite.getEmail());
        return genCurrentBillExplanation(userRecord);
    }

    //@Override
    public Optional<ImaniBillExplained> genCurrentBillExplanation(UserRecord userRecord) {
        Assert.notNull(userRecord, "UserRecord cannot be null");
        DateTime dateTimeAtStartOfMonth = iDateTimeUtil.getDateTimeAtStartOfMonth(DateTime.now());
        LOGGER.info("Generating lease agreement bill for user: {} for current month: {}", userRecord.getEmbeddedContactInfo().getEmail(), dateTimeAtStartOfMonth.monthOfYear().getAsShortText());

        ImaniBill imaniBill = imaniBillRepository.getImaniBillForScheduleDate(userRecord, dateTimeAtStartOfMonth);
        if(imaniBill != null) {
            return Optional.of(imaniBill.toImaniBillExplained());
        }

        return Optional.empty();
    }

    Property getLeaseProperty(PropertyLeaseAgreement propertyLeaseAgreement) {
        if(propertyLeaseAgreement.getLeasedApartment() != null) {
            LOGGER.info("Found lease agreement on apartment");
            return propertyLeaseAgreement.getLeasedApartment().getFloor().getProperty();
        } else {
            return propertyLeaseAgreement.getLeasedProperty();
        }
    }

    ImaniBill createLeaseImaniBill(UserRecord userRecord, PropertyLeaseAgreement propertyLeaseAgreement, DateTime dateTimeAtStartOfMonth) {
        LOGGER.info("Generating a new lease Imani Bill for user:=> {} for dateTimeAtStartOfMonth:=> {}", userRecord.getEmbeddedContactInfo().getEmail(), dateTimeAtStartOfMonth);
        ImaniBill imaniBill = ImaniBill.builder()
                .billScheduleDate(dateTimeAtStartOfMonth)
                .billScheduleTypeE(BillScheduleTypeE.MONTHLY)
                .billServiceRenderedTypeE(BillServiceRenderedTypeE.Residential_Lease)
                .billedUser(userRecord)
                .amountOwed(propertyLeaseAgreement.getEmbeddedAgreement().getFixedCost())
                .propertyLeaseAgreement(propertyLeaseAgreement)
                .amountPaid(0.0)
                .build();
        return imaniBill;
    }


}

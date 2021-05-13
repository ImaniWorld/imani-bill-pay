package com.imani.bill.pay.service.billing;

import com.imani.bill.pay.domain.billing.BillScheduleTypeE;
import com.imani.bill.pay.domain.billing.BillServiceRenderedTypeE;
import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.leasemanagement.PropertyLeaseAgreement;
import com.imani.bill.pay.domain.property.Property;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.repository.IUserRecordRepository;
import com.imani.bill.pay.domain.user.repository.IUserResidenceRepository;
import com.imani.bill.pay.service.util.DateTimeUtil;
import com.imani.bill.pay.service.util.IDateTimeUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * This implementation generates user lease agreement bills.
 *
 * @author manyce400
 */
@Service(ResidentialPropertyLeaseBillGenerationService.SPRING_BEAN)
public class ResidentialPropertyLeaseBillGenerationService implements IBillGenerationService {



    @Autowired
    @Qualifier(DateTimeUtil.SPRING_BEAN)
    private IDateTimeUtil iDateTimeUtil;

    @Autowired
    private IUserRecordRepository iUserRecordRepository;

    @Autowired
    @Qualifier(ImaniBillService.SPRING_BEAN)
    private IImaniBillService imaniBillService;

    @Autowired
    private IUserResidenceRepository iUserResidenceRepository;

    @Autowired
    @Qualifier(ResidentialPropertyLeaseFeeGenerationService.SPRING_BEAN)
    private IBillPayFeeGenerationService iBillPayFeeGenerationService;


    public static final String SPRING_BEAN = "com.imani.bill.pay.service.billing.ResidentialPropertyLeaseBillGenerationService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ResidentialPropertyLeaseBillGenerationService.class);


    @Override
    public boolean generateImaniBill(Object generationObject) {
        return false;
    }

    Property getLeaseProperty(PropertyLeaseAgreement propertyLeaseAgreement) {
        if(propertyLeaseAgreement.getLeasedApartment() != null) {
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
                .amountOwed(propertyLeaseAgreement.getEmbeddedAgreement().getFixedCost())
                .propertyLeaseAgreement(propertyLeaseAgreement)
                .amountPaid(0.0)
                .build();
        return imaniBill;
    }


}

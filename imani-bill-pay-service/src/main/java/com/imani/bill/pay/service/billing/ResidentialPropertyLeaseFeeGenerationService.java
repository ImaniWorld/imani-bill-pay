package com.imani.bill.pay.service.billing;

import com.imani.bill.pay.domain.agreement.IHasBillingAgreement;
import com.imani.bill.pay.domain.billing.BillPayFeeExplained;
import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.billing.repository.IBillPayFeeRepository;
import com.imani.bill.pay.domain.leasemanagement.PropertyLeaseAgreement;
import com.imani.bill.pay.domain.property.Property;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.service.util.DateTimeUtil;
import com.imani.bill.pay.service.util.IDateTimeUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

/**
 * @author manyce400
 */
@Service(ResidentialPropertyLeaseFeeGenerationService.SPRING_BEAN)
public class ResidentialPropertyLeaseFeeGenerationService implements IBillPayFeeGenerationService {


    @Autowired
    private IBillPayFeeRepository iBillPayFeeRepository;

    @Autowired
    @Qualifier(DateTimeUtil.SPRING_BEAN)
    private IDateTimeUtil iDateTimeUtil;

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.billing.ResidentialPropertyLeaseFeeGenerationService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ResidentialPropertyLeaseFeeGenerationService.class);


    @Override
    public void addImaniBillFees(UserRecord userRecord, IHasBillingAgreement iHasBillingAgreement, ImaniBill imaniBill) {

    }

    @Override
    public void addImaniBillFees(UserRecord userRecord, PropertyLeaseAgreement propertyLeaseAgreement, ImaniBill imaniBill) {
        Assert.notNull(userRecord, "UserRecord cannot be null");
        Assert.notNull(propertyLeaseAgreement, "LeaseAgreement cannot be null");
        Assert.notNull(imaniBill, "ImaniBill cannot be null");

        // TODO apply logic to check agreement billing schedule

        if (!imaniBill.isPaidInFull()) {
            Property property = propertyLeaseAgreement.getLeasedApartment().getFloor().getProperty();
            LOGGER.info("Generating and applying lease fees for user:=> {}", userRecord.getEmbeddedContactInfo().getEmail());

            // Apply any late fee where applicable
            applyLeaseLateFee(imaniBill, property, propertyLeaseAgreement);
        }
    }

    @Override
    public Optional<List<BillPayFeeExplained>> explainImaniBillFees(UserRecord userRecord, PropertyLeaseAgreement propertyLeaseAgreement, ImaniBill imaniBill) {
        Assert.notNull(userRecord, "UserRecord cannot be null");
        Assert.notNull(propertyLeaseAgreement, "LeaseAgreement cannot be null");
        Assert.notNull(imaniBill, "ImaniBill cannot be null");

        LOGGER.debug("Attempting to generate bill explanation for imaniBill:=> {}", imaniBill);

        return Optional.empty();
    }

    void applyLeaseLateFee(ImaniBill imaniBill, Property property, PropertyLeaseAgreement propertyLeaseAgreement) {
//        boolean isCurrentMonthLeasePaymentLate = isCurrentMonthLeasePaymentLate(property, imaniBill);
//
//        if (isCurrentMonthLeasePaymentLate) {
//            Set<ImaniBillToFee> appliedLateFees = imaniBill.getBillPayFeesByFeeTypeE(FeeTypeE.LATE_FEE);
//
//            if(CollectionUtils.isEmpty(appliedLateFees)) {
//                LOGGER.info("User lease payment is late, applying late fee for this month's payment...");
//                BillPayFee lateBillPayFee = iBillPayFeeRepository.findBillPayFeeByFeeType(property, FeeTypeE.LATE_FEE);
//                Double feeAmount = lateBillPayFee.calculatFeeCharge(imaniBill.getAmountOwed());
//
//                // Calculate charge with fee
//                double amountWithFeeCharge = propertyLeaseAgreement.getEmbeddedAgreement().getFixedCost().doubleValue() + feeAmount;
//
//                imaniBill.setAmountOwed(amountWithFeeCharge);
//                imaniBill.addImaniBillToFee(lateBillPayFee, feeAmount);
//            } else {
//                LOGGER.info("ImaniBill => {} for lease agreement already has late fee applied, skipping fees processing", imaniBill.getId());
//            }
//        }
    }

    boolean isCurrentMonthLeasePaymentLate(Property property, ImaniBill imaniBill) {
        DateTime now = DateTime.now().withTimeAtStartOfDay();
        DateTime dateTimeAtStartOfMonth = iDateTimeUtil.getDateTimeAtStartOfMonth(now);

        // Calculate number of days between the start of the rental month and today's date and compare that to property grace period
        Integer daysBetween = iDateTimeUtil.getDaysBetweenDates(dateTimeAtStartOfMonth, now);
        LOGGER.info("Property Max-Number of days late:  {} User Days-Late:= {}", property.getMthlyNumberOfDaysPaymentLate(), daysBetween);
        return daysBetween > property.getMthlyNumberOfDaysPaymentLate();
    }


}

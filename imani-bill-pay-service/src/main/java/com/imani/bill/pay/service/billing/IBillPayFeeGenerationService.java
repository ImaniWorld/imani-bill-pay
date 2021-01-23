package com.imani.bill.pay.service.billing;

import com.imani.bill.pay.domain.agreement.EmbeddedAgreement;
import com.imani.bill.pay.domain.agreement.IHasBillingAgreement;
import com.imani.bill.pay.domain.billing.*;
import com.imani.bill.pay.domain.leasemanagement.PropertyLeaseAgreement;
import com.imani.bill.pay.domain.user.UserRecord;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author manyce400
 */
public interface IBillPayFeeGenerationService {

    static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(IBillPayFeeGenerationService.class);

    public void addImaniBillFees(UserRecord userRecord, IHasBillingAgreement iHasBillingAgreement, ImaniBill imaniBill);

    public void addImaniBillFees(UserRecord userRecord, PropertyLeaseAgreement propertyLeaseAgreement, ImaniBill imaniBill);

    public Optional<List<BillPayFeeExplained>> explainImaniBillFees(UserRecord userRecord, PropertyLeaseAgreement propertyLeaseAgreement, ImaniBill imaniBill);

    public default boolean isImaniBillPaymentLate(Integer numberOfDaysLate,  Integer maxAgreementDaysLate) {
        return numberOfDaysLate > maxAgreementDaysLate;
    }

    public default void applyLateFee(ImaniBill imaniBill, EmbeddedAgreement embeddedAgreement, BillPayFee billPayFee) {
        Double feeAmount = billPayFee.calculatFeeCharge(imaniBill.getAmountOwed());
        BillScheduleTypeE billScheduleTypeE = embeddedAgreement.getBillScheduleTypeE();

        DateTime now = DateTime.now();

        // Find all Late fees levied against this bill to understand IF we can apply more late fees
        Set<ImaniBillToFee> imaniBillToFees = imaniBill.getBillPayFeesByFeeTypeE(FeeTypeE.LATE_FEE);
        LOGGER.info("Found total number of fees already levied against bill => {}", imaniBillToFees.size());

        // Search through all levied fees by either Month or Week
        // IF a fee has already been applied during the Month or Week then dont apply another fee
        // TODO: Figure out how to handle applying fees on items billed weekly
        boolean applyFee = true;
        for(ImaniBillToFee imaniBillToFee : imaniBillToFees) {
            DateTime leviedDate = imaniBillToFee.getCreateDate();
            switch (billScheduleTypeE) {
                case MONTHLY:
                    if(leviedDate.getMonthOfYear() == now.getMonthOfYear()
                            && leviedDate.getYear() == now.getYear()) {
                        // Found a fee levied in this month so dont apply another fee
                        LOGGER.info("Found a fee levied against bill in current month, skipping applying additional fee since in the same month.");
                        applyFee = false;
                        break;
                    }
                    break;
                default:
                    break;
            }
        }

        if (applyFee) {
            double billAmountWithFeeCharged = embeddedAgreement.getFixedCost().doubleValue() + feeAmount;
            imaniBill.setAmountOwed(billAmountWithFeeCharged);
            imaniBill.addImaniBillToFee(billPayFee, feeAmount);
        }
    }

}
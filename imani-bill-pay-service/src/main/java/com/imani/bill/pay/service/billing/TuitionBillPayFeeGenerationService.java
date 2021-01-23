package com.imani.bill.pay.service.billing;

import com.imani.bill.pay.domain.agreement.IHasBillingAgreement;
import com.imani.bill.pay.domain.billing.*;
import com.imani.bill.pay.domain.billing.repository.IBillPayFeeRepository;
import com.imani.bill.pay.domain.business.Business;
import com.imani.bill.pay.domain.education.TuitionAgreement;
import com.imani.bill.pay.domain.leasemanagement.PropertyLeaseAgreement;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.service.education.ITuitionAgreementService;
import com.imani.bill.pay.service.education.TuitionAgreementService;
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
@Service(TuitionBillPayFeeGenerationService.SPRING_BEAN)
public class TuitionBillPayFeeGenerationService implements IBillPayFeeGenerationService {


    @Autowired
    @Qualifier(DateTimeUtil.SPRING_BEAN)
    private IDateTimeUtil iDateTimeUtil;

    @Autowired
    private IBillPayFeeRepository iBillPayFeeRepository;

    @Autowired
    @Qualifier(TuitionAgreementService.SPRING_BEAN)
    private ITuitionAgreementService iTuitionAgreementService;

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.billing.TuitionBillPayFeeGenerationService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(TuitionBillPayFeeGenerationService.SPRING_BEAN);


    @Override
    public void addImaniBillFees(UserRecord userRecord, IHasBillingAgreement iHasBillingAgreement, ImaniBill imaniBill) {
        Assert.notNull(userRecord, "UserRecord cannot be null");
        Assert.notNull(iHasBillingAgreement, "iHasBillingAgreement cannot be null");
        Assert.notNull(imaniBill, "imaniBill cannot be null");

        if (!imaniBill.isPaidInFull()) {
            TuitionAgreement tuitionAgreement = (TuitionAgreement) iHasBillingAgreement;
            Business school = tuitionAgreement.getBusiness();

            String[] args = {tuitionAgreement.getStudent().getFullName(), userRecord.getEmbeddedContactInfo().getEmail(), imaniBill.getId().toString()};
            LOGGER.info("Attempting to add Tuition Bill Pay Fees for student[{}] to be paid by user => {} on ImaniBill[{}]", args);

            // Check to see IF the bill is actually late and get the number of days late
            DateTime now = DateTime.now().withTimeAtStartOfDay();
            DateTime billScheduleDate = imaniBill.getBillScheduleDate();
            Integer maxDaysLate = iTuitionAgreementService.findNumberOfDaysTillPaymentLate(tuitionAgreement);
            Integer daysBetweenDueAndNow = iDateTimeUtil.getDaysBetweenDates(billScheduleDate, now);
            boolean isBillLate = isImaniBillPaymentLate(daysBetweenDueAndNow, maxDaysLate);

            if(isBillLate) {
                LOGGER.info("Detected that ImaniBill[{}] is late executing logic to apply late fee", imaniBill.getId());
                Optional<BillPayFee> lateBillPayFee = iBillPayFeeRepository.findBillPayFeeByFeeType(school, FeeTypeE.LATE_FEE);

                if(lateBillPayFee.isPresent()) {
                    LOGGER.info("School has configured a fee:=> {}", lateBillPayFee.get());
                    applyLateFee(imaniBill, tuitionAgreement.getEmbeddedAgreement(), lateBillPayFee.get());
                } else {
                    LOGGER.warn("*** Failed to apply late fee.  Imani Bill payment is past due but no late fee has been configured by School:=> {} ****", school.getName());
                }
            }
        }
    }

    @Override
    public void addImaniBillFees(UserRecord userRecord, PropertyLeaseAgreement propertyLeaseAgreement, ImaniBill imaniBill) {

    }

    @Override
    public Optional<List<BillPayFeeExplained>> explainImaniBillFees(UserRecord userRecord, PropertyLeaseAgreement propertyLeaseAgreement, ImaniBill imaniBill) {
        return Optional.empty();
    }
}

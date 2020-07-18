package com.imani.bill.pay.service.property;

import com.google.common.collect.ImmutableList;
import com.imani.bill.pay.domain.property.*;
import com.imani.bill.pay.domain.property.repository.IMonthlyRentalFeeRepository;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.UserResidence;
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
@Service(MonthlyRentalFeeService.SPRING_BEAN)
public class MonthlyRentalFeeService implements IMonthlyRentalFeeService {



    @Autowired
    private IMonthlyRentalFeeRepository iMonthlyRentalFeeRepository;

    @Autowired
    @Qualifier(DateTimeUtil.SPRING_BEAN)
    private IDateTimeUtil iDateTimeUtil;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MonthlyRentalFeeService.class);

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.property.MonthlyRentalFeeService";



    @Override
    public Optional<List<MonthlyRentalFeeExplained>> applyMonthlyRentalFees(UserResidence userResidence, MonthlyRentalBill monthlyRentalBill) {
        Assert.notNull(userResidence, "UserResidence cannot be null");
        Assert.notNull(monthlyRentalBill, "MonthlyRentalBill cannot be null");

        UserRecord userRecord = userResidence.getUserRecord();

        LOGGER.info("Attempting to apply rental fee's to monthlyRentalBill month: {} for user: {}", monthlyRentalBill.getRentalMonth(), userRecord.getEmbeddedContactInfo().getEmail());

        // Apply late fee charge if this bill is currently late
        Property leaseProperty = userResidence.getApartment().getFloor().getProperty();
        boolean isCurrentMonthPaymentLate = isCurrentMonthPaymentLate(leaseProperty, monthlyRentalBill);

        if(isCurrentMonthPaymentLate) {
            MonthlyRentalFee lateRentalFee = iMonthlyRentalFeeRepository.findPropertyMonthlyRentalFeeByType(leaseProperty, RentalFeeTypeE.LATE_FEE);

            if(lateRentalFee != null) {
                LOGGER.info("Adding lateRentalFee:=> {} to monthly bill", lateRentalFee);
                monthlyRentalBill.addMonthlyRentalFee(lateRentalFee);

                // Build fee explanation object
                Double feeCharge = lateRentalFee.calculatFeeCharge(monthlyRentalBill.getLeaseAgreement().getMonthlyRentalCost());
                MonthlyRentalFeeExplained lateMonthlyRentalFeeExplained = MonthlyRentalFeeExplained.builder()
                        .feeName(lateRentalFee.getFeeName())
                        .feeCharge(feeCharge)
                        .feeAppliedDate(DateTime.now().withTimeAtStartOfDay())
                        .build();
                return Optional.of(ImmutableList.of(lateMonthlyRentalFeeExplained));
            } else {
                throw new RuntimeException("Cannot apply late fee to monthly rental amount, no configured fee found for property");
            }
        }

        return Optional.empty();
    }

    boolean isCurrentMonthPaymentLate(Property property, MonthlyRentalBill monthlyRentalBill) {
        System.out.println("Passed from call to compute property = " + property);
        DateTime now = DateTime.now().withTimeAtStartOfDay();
        DateTime dateTimeAtStartOfMonth = monthlyRentalBill.getRentalMonth();

        // Calculate number of days between the start of the rental month and today's date and compare that to property grace period
        Integer daysBetween = iDateTimeUtil.getDaysBetweenDates(dateTimeAtStartOfMonth, now);
        LOGGER.info("Property number of days late:=> {} daysBetween:=> {}", property.getMthlyNumberOfDaysPaymentLate(), daysBetween);
        return daysBetween > property.getMthlyNumberOfDaysPaymentLate();
    }
}

package com.imani.bill.pay.service.property;

import com.imani.bill.pay.domain.property.*;
import com.imani.bill.pay.domain.property.repository.IMonthlyRentalBillRepository;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.UserResidence;
import com.imani.bill.pay.domain.user.UserResidencePropertyService;
import com.imani.bill.pay.domain.user.repository.IUserResidenceRepository;
import com.imani.bill.pay.service.util.DateTimeUtil;
import com.imani.bill.pay.service.util.IDateTimeUtil;
import org.apache.commons.math3.stat.descriptive.summary.Sum;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author manyce400
 */
@Service(MonthlyRentalBillDescService.SPRING_BEAN)
public class MonthlyRentalBillDescService implements IMonthlyRentalBillDescService {



    @Autowired
    @Qualifier(DateTimeUtil.SPRING_BEAN)
    private IDateTimeUtil iDateTimeUtil;

    @Autowired
    private IMonthlyRentalBillRepository iMonthlyRentalBillRepository;

    @Autowired
    private IUserResidenceRepository iUserResidenceRepository;

    @Autowired
    @Qualifier(MonthlyRentalFeeService.SPRING_BEAN)
    private IMonthlyRentalFeeService iMonthlyRentalFeeService;

    @Autowired
    @Qualifier(MonthlyPropertySvcChargeService.SPRING_BEAN)
    private IMonthlyPropertySvcChargeService iMonthlyPropertySvcChargeService;


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MonthlyRentalBillDescService.class);

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.property.MonthlyRentalBillDescService";


    @Transactional
    @Override
    public Optional<MonthlyRentalBillExplained> getCurrentMonthRentalBill(UserRecord userRecord) {
        Assert.notNull(userRecord, "UserRecord cannot be null");

        // Find the UserResidence and other relevant info to generate current month bill
        UserResidence userResidence = iUserResidenceRepository.findUserResidence(userRecord);
        RentalAgreement rentalAgreement = userResidence.getRentalAgreement();

        if (rentalAgreement != null && rentalAgreement.isAgreementInEffect()) {
            // Current Month DateTime should start at the start of the month.  All bills are due 1'st day of month
            DateTime dateTimeAtStartOfMonth = iDateTimeUtil.getDateTimeAtStartOfMonth(DateTime.now());

            // First check to see if a bill has already been generated for this user for calculated start of month datetime
            MonthlyRentalBill existingMonthlyRentalBill = iMonthlyRentalBillRepository.getUserMonthlyRentalBill(userRecord, dateTimeAtStartOfMonth);

            if(existingMonthlyRentalBill == null) {
                LOGGER.info("Generating new monthly rental bill for user:=> ", userRecord.getEmbeddedContactInfo().getEmail());


                MonthlyRentalBill monthlyRentalBill = MonthlyRentalBill.builder()
                        .rentalAgreement(rentalAgreement)
                        .amountPaid(0.0)
                        .rentalMonth(dateTimeAtStartOfMonth)
                        .userResidence(userResidence)
                        .rentalAgreement(rentalAgreement)
                        .billClosed(false)
                        .build();

                // Get fees and property charges details
                Optional<List<MonthlyRentalFeeExplained>> monthlyRentalFeeExplainedList = iMonthlyRentalFeeService.applyMonthlyRentalFees(userResidence, monthlyRentalBill);
                Optional<List<PropertyServiceChargeExplained>> propertyServiceChargeExplainedList = iMonthlyPropertySvcChargeService.applyMonthlyPropertyServiceCharge(userResidence, monthlyRentalBill);

                // Get explanation and return
                MonthlyRentalBillExplained monthlyRentalBillExplained = getMonthlyRentalBillExplained(userResidence, monthlyRentalFeeExplainedList, propertyServiceChargeExplainedList);
                monthlyRentalBillExplained.setRentalMonth(dateTimeAtStartOfMonth);

                LOGGER.debug("Saving generated new monthly rental bill: {}", monthlyRentalBill);
                iMonthlyRentalBillRepository.save(monthlyRentalBill);
                return Optional.of(monthlyRentalBillExplained);
            } else {
                LOGGER.warn("Cannot generate new monthly bill.  RentalAgreement is currently not in effect for user:=> {}", userRecord.getEmbeddedContactInfo().getEmail());
            }
        }

        return Optional.empty();
    }

    @Override
    public Double calculateTotalAmountDue(MonthlyRentalBill monthlyRentalBill, Set<UserResidencePropertyService> userResidencePropertyServices) {
        Assert.notNull(monthlyRentalBill, "monthlyRentalBill cannot be null");
        Assert.notNull(userResidencePropertyServices, "userResidencePropertyServices cannot be null");

        // Get the monthly rental this user is supposed to pay by their rental agreement
        Double monthlyRentalCost = monthlyRentalBill.getRentalAgreement().getMonthlyRentalCost();
        LOGGER.info("Calculating total monthly amount due on bill with monthly rental agreement amount:=> {}", monthlyRentalCost);

        Sum sum = new Sum();
        sum.increment(monthlyRentalCost);

        // Calculate the total from monthly rental fees
        Set<MonthlyRentalBillFee> monthlyRentalBillFees = monthlyRentalBill.getMonthlyRentalBillFees();
        monthlyRentalBillFees.forEach(monthlyRentalBillFee -> {
            double paymentFees = monthlyRentalBillFee.getMonthlyRentalFee().calculatFeeCharge(monthlyRentalCost);
            sum.increment(paymentFees);
        });

        // Calculate the total from Property Services passed as argument
        userResidencePropertyServices.forEach(userResidencePropertyService -> {
            sum.increment(userResidencePropertyService.getPropertyService().getServiceMonthlyCost());
        });

        return sum.getResult();
    }

    MonthlyRentalBillExplained getMonthlyRentalBillExplained(UserResidence userResidence, Optional<List<MonthlyRentalFeeExplained>> monthlyRentalFeeExplainedList, Optional<List<PropertyServiceChargeExplained>> propertyServiceChargeExplainedList) {
        MonthlyRentalBillExplained monthlyRentalBillExplained = MonthlyRentalBillExplained.builder()
                .monthlyRentalCost(userResidence.getRentalAgreement().getMonthlyRentalCost())
                .amtPaid(0.0)
                .totalAmtDue(userResidence.getRentalAgreement().getMonthlyRentalCost()) // initialize total amount due to monthly rental cost from RentalAgreement
                .userResidence(userResidence)
                .build();

        // Figure out total fees
        applyTotalRentalFeesCharged(monthlyRentalBillExplained, monthlyRentalFeeExplainedList);
        applyTotalPropertyServiceCostCharged(monthlyRentalBillExplained, propertyServiceChargeExplainedList);
        return monthlyRentalBillExplained;
    }


    void applyTotalRentalFeesCharged(MonthlyRentalBillExplained monthlyRentalBillExplained, Optional<List<MonthlyRentalFeeExplained>> optional) {
        if(optional.isPresent()) {
            List<MonthlyRentalFeeExplained> monthlyRentalFeeExplainedList = optional.get();
            Sum sum = new Sum();
            sum.increment(monthlyRentalBillExplained.getTotalAmtDue());

            monthlyRentalFeeExplainedList.forEach(monthlyRentalFeeExplained -> {
                sum.increment(monthlyRentalFeeExplained.getFeeCharge());
                monthlyRentalBillExplained.addMonthlyRentalFeeExplained(monthlyRentalFeeExplained);
            });

            monthlyRentalBillExplained.setTotalAmtDue(sum.getResult());
        }

    }

    void applyTotalPropertyServiceCostCharged(MonthlyRentalBillExplained monthlyRentalBillExplained, Optional<List<PropertyServiceChargeExplained>> optional) {
        if(optional.isPresent()) {
            List<PropertyServiceChargeExplained> propertyServiceChargeExplainedList = optional.get();
            Sum sum = new Sum();
            sum.increment(monthlyRentalBillExplained.getTotalAmtDue());

            propertyServiceChargeExplainedList.forEach(propertyServiceChargeExplained -> {
                sum.increment(propertyServiceChargeExplained.getServiceMonthlyCost());
                monthlyRentalBillExplained.addPropertyServiceChargeExplained(propertyServiceChargeExplained);
            });

            monthlyRentalBillExplained.setTotalAmtDue(sum.getResult());
        }
    }


}
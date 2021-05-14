package com.imani.bill.pay.service.billing.compute;

import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.billing.repository.IImaniBillRepository;
import com.imani.bill.pay.domain.billing.repository.IImaniBillWaterSvcAgreementRepository;
import com.imani.bill.pay.domain.utility.WaterServiceAgreement;
import com.imani.bill.pay.domain.utility.WaterUtilizationCharge;
import com.imani.bill.pay.domain.utility.repository.IWaterUtilizationChargeRepository;
import com.imani.bill.pay.service.util.DateTimeUtil;
import com.imani.bill.pay.service.util.IDateTimeUtil;
import com.imani.bill.pay.service.utility.IWaterUtilizationService;
import com.imani.bill.pay.service.utility.WaterUtilizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author manyce400
 */
@Service(WaterBillingComputeService.SPRING_BEAN)
public class WaterBillingComputeService implements IBillingComputeService<WaterServiceAgreement> {


    @Autowired
    private IImaniBillWaterSvcAgreementRepository iImaniBillWaterSvcAgreementRepository;

    @Autowired
    private IImaniBillRepository imaniBillRepository;

    @Autowired
    private IWaterUtilizationChargeRepository iWaterUtilizationChargeRepository;

    @Autowired
    @Qualifier(WaterUtilizationService.SPRING_BEAN)
    private IWaterUtilizationService iWaterUtilizationService;

    @Autowired
    @Qualifier(BillingFeeComputeService.SPRING_BEAN)
    private IBillingFeeComputeService iBillingFeeComputeService;

    @Autowired
    @Qualifier(DateTimeUtil.SPRING_BEAN)
    private IDateTimeUtil iDateTimeUtil;

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.billing.compute.WaterBillingComputeService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(WaterBillingComputeService.class);


    @Transactional
    @Override
    public void computeUpdateAgreementBills(WaterServiceAgreement agreement) {
        Assert.notNull(agreement, "WaterServiceAgreement cannot be null");

        LOGGER.info("Computing and updating all unpaid WaterService ImaniBills for {}", agreement.describeAgreement());

        // Find all unpaid bills on the WaterServiceAgreement and recompute all fee's.  Expectation is late fees will be applied
        List<ImaniBill> unPaidImaniBills = iImaniBillWaterSvcAgreementRepository.findAllAgreementUnPaidBills(agreement.getId());
        unPaidImaniBills.forEach(unPaidImaniBill -> {
            computeUpdateBill(agreement, unPaidImaniBill);
            LOGGER.info("========================================================================================");
        });
    }

    @Transactional
    @Override
    public void computeUpdateBill(WaterServiceAgreement agreement, ImaniBill imaniBill) {
        Assert.notNull(imaniBill, "ImaniBill cannot be null");

        if (!imaniBill.isPaidInFull()) {
            Object[] args = {imaniBill.getId(), imaniBill.getAmountOwed(), imaniBill.getAmountPaid()};
            LOGGER.info("Recomputing full charges on ImaniBill[ID: {} || AmtOwed: {} | AmtPaid: {}]", args);
            imaniBill.setAmountOwed(0d);


            // Compute and update amount owed from scheduled fees
            iBillingFeeComputeService.computeUpdateAmountOwedWithSchedFees(agreement.getScheduledBillPayFees(), imaniBill);

            // Next compute the current water utilization in the quarter in order to add to computed charges
            WaterUtilizationCharge waterUtilizationCharge = iWaterUtilizationService.computeUpdateWithUtilizationCharge(imaniBill);

            // Last step, we can now compute late fees against new charges computed with scheduled fees. Save and flush all changes to Bill
            iBillingFeeComputeService.computeUpdateAmountOwedWithLateFee(agreement.getEmbeddedAgreement(), agreement.getEmbeddedUtilityService(), imaniBill);

            // Save all details
            imaniBillRepository.save(imaniBill);
            iWaterUtilizationChargeRepository.save(waterUtilizationCharge);
        }
    }

}
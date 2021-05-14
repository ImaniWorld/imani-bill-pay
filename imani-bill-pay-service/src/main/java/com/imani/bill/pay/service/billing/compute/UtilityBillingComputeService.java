package com.imani.bill.pay.service.billing.compute;

import com.imani.bill.pay.domain.agreement.EmbeddedAgreement;
import com.imani.bill.pay.domain.billing.BillPayFee;
import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.billing.repository.IImaniBillRepository;
import com.imani.bill.pay.domain.billing.repository.IImaniBillSewerSvcAgreementRepository;
import com.imani.bill.pay.domain.billing.repository.IImaniBillWaterSvcAgreementRepository;
import com.imani.bill.pay.domain.utility.EmbeddedUtilityService;
import com.imani.bill.pay.domain.utility.SewerServiceAgreement;
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
@Service(UtilityBillingComputeService.SPRING_BEAN)
public class UtilityBillingComputeService implements IBillingComputeService {


    @Autowired
    private IImaniBillWaterSvcAgreementRepository iImaniBillWaterSvcAgreementRepository;

    @Autowired
    private IImaniBillSewerSvcAgreementRepository iImaniBillSewerSvcAgreementRepository;

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

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.billing.compute.UtilityBillingComputeService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(UtilityBillingComputeService.class);


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

    @Override
    public void computeUpdateAgreementBills(SewerServiceAgreement agreement) {
        Assert.notNull(agreement, "SewerServiceAgreement cannot be null");

        LOGGER.info("Computing and updating all unpaid SewerServiceAgreement ImaniBills for {}", agreement.describeAgreement());

        // Find all unpaid bills on the WaterServiceAgreement and recompute all fee's.  Expectation is late fees will be applied
        List<ImaniBill> unPaidImaniBills = iImaniBillSewerSvcAgreementRepository.findAllAgreementUnPaidBills(agreement.getId());
        unPaidImaniBills.forEach(unPaidImaniBill -> {
            computeUpdateBill(agreement, unPaidImaniBill);
            LOGGER.info("========================================================================================");
        });
    }



    @Transactional
    @Override
    public void computeUpdateBill(WaterServiceAgreement waterServiceAgreement, ImaniBill imaniBill) {
        Assert.notNull(waterServiceAgreement, "waterServiceAgreement cannot be null");
        Assert.notNull(imaniBill, "ImaniBill cannot be null");
        computeUpdateBill(waterServiceAgreement.getEmbeddedAgreement(), waterServiceAgreement.getEmbeddedUtilityService(), imaniBill, waterServiceAgreement.getScheduledBillPayFees(), true);
    }

    @Override
    public void computeUpdateBill(SewerServiceAgreement sewerServiceAgreement, ImaniBill imaniBill) {
        Assert.notNull(sewerServiceAgreement, "SewerServiceAgreement cannot be null");
        Assert.notNull(imaniBill, "ImaniBill cannot be null");
        computeUpdateBill(sewerServiceAgreement.getEmbeddedAgreement(), sewerServiceAgreement.getEmbeddedUtilityService(), imaniBill, sewerServiceAgreement.getScheduledBillPayFees(), false);
    }

    private void computeUpdateBill(EmbeddedAgreement embeddedAgreement, EmbeddedUtilityService embeddedUtilityService, ImaniBill imaniBill, List<BillPayFee> billPayFees, boolean computeWaterUtilization) {
        if (!imaniBill.isPaidInFull()) {
            Object[] args = {imaniBill.getId(), imaniBill.getAmountOwed(), imaniBill.getAmountPaid()};
            LOGGER.info("Recomputing full charges on ImaniBill[ID: {} || AmtOwed: {} | AmtPaid: {}]", args);
            imaniBill.setAmountOwed(0d);


            // Compute and update amount owed from scheduled fees
            iBillingFeeComputeService.computeUpdateAmountOwedWithSchedFees(billPayFees, imaniBill);
            WaterUtilizationCharge waterUtilizationCharge = null;

            if (computeWaterUtilization) {
                // Next compute the current water utilization in the quarter in order to add to computed charges
                waterUtilizationCharge = iWaterUtilizationService.computeUpdateWithUtilizationCharge(imaniBill);
            }

            // Last step, we can now compute late fees against new charges computed with scheduled fees. Save and flush all changes to Bill
            iBillingFeeComputeService.computeUpdateAmountOwedWithLateFee(embeddedAgreement, embeddedUtilityService, imaniBill);

            // Save all details
            imaniBillRepository.save(imaniBill);
            iWaterUtilizationChargeRepository.save(waterUtilizationCharge);
        }
    }

}
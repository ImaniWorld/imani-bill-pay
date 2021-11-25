package com.imani.bill.pay.service.billing.utility;

import com.imani.bill.pay.domain.billing.BillScheduleTypeE;
import com.imani.bill.pay.domain.billing.BillServiceRenderedTypeE;
import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.billing.repository.IImaniBillWaterSvcAgreementRepository;
import com.imani.bill.pay.domain.utility.WaterServiceAgreement;
import com.imani.bill.pay.service.billing.IBillGenerationService;
import com.imani.bill.pay.service.billing.compute.IBillingComputeService;
import com.imani.bill.pay.service.billing.compute.UtilityBillingComputeService;
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
 * @author manyce400
 */
@Service(WaterSvcBillGenerationService.SPRING_BEAN)
public class WaterSvcBillGenerationService  implements IBillGenerationService<WaterServiceAgreement> {



    @Autowired
    private IImaniBillWaterSvcAgreementRepository imaniBillWaterSvcAgreementRepository;

    @Autowired
    @Qualifier(DateTimeUtil.SPRING_BEAN)
    private IDateTimeUtil iDateTimeUtil;

    @Autowired
    @Qualifier(UtilityBillingComputeService.SPRING_BEAN)
    private IBillingComputeService iBillingComputeService;


    public static final String SPRING_BEAN = "com.imani.bill.pay.service.billing.utility.WaterSvcBillGenerationService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(WaterSvcBillGenerationService.class);



    /**
     *
     * @param waterServiceAgreement
     * @return
     */
    @Transactional
    @Override
    public boolean generateImaniBill(WaterServiceAgreement waterServiceAgreement) {
        Assert.notNull(waterServiceAgreement, "WaterServiceAgreement cannot be null");

        if(waterServiceAgreement.getEmbeddedAgreement().getBillScheduleTypeE() == BillScheduleTypeE.QUARTERLY) {
            // Current bill will always be due at the start of next quarter based on this quarter's utilization
            DateTime billScheduleDate = iDateTimeUtil.getDateTimeAStartOfNextQuarter();
            Optional<ImaniBill> imaniBill = getImaniBillForEntity(waterServiceAgreement, billScheduleDate);

            if(!imaniBill.isPresent()) {
                LOGGER.info("Generating new WaterService ImaniBill in current quarter for Schedule-Date[{}] on {}", billScheduleDate, waterServiceAgreement.describeAgreement());
                ImaniBill persistedBill = generateImaniBill(waterServiceAgreement, billScheduleDate);
                iBillingComputeService.computeUpdateBill(waterServiceAgreement, persistedBill);
            } else {
                // Compute and update all water bills for this agreement
                iBillingComputeService.computeUpdateAgreementBills(waterServiceAgreement);
            }

            return true;
        }

        return false;
    }

    private Optional<ImaniBill> getImaniBillForEntity(WaterServiceAgreement waterServiceAgreement, DateTime billScheduleDate) {
        if(waterServiceAgreement.getEmbeddedAgreement().isBilledEntityUser()) {
            LOGGER.info("Agreement is billed to User. Finding existing bill in current Quarter....");
            return imaniBillWaterSvcAgreementRepository.getImaniBillForAgreement(waterServiceAgreement.getEmbeddedAgreement().getAgreementUserRecord(), waterServiceAgreement, billScheduleDate);
        } else if(waterServiceAgreement.getEmbeddedAgreement().isBilledEntityBusiness()) {
            LOGGER.info("Agreement is billed to a Business. Finding existing bill in current Quarter....");
            return imaniBillWaterSvcAgreementRepository.getImaniBillForAgreement(waterServiceAgreement.getEmbeddedAgreement().getAgreementBusiness(), waterServiceAgreement, billScheduleDate);
        } else if(waterServiceAgreement.getEmbeddedAgreement().isBilledEntityCommunity()) {
            LOGGER.info("Agreement is billed to a Community. Finding existing bill in current Quarter....");
            return imaniBillWaterSvcAgreementRepository.getImaniBillForAgreement(waterServiceAgreement.getEmbeddedAgreement().getAgreementCommunity(), waterServiceAgreement, billScheduleDate);
        } else if(waterServiceAgreement.getEmbeddedUtilityService().hasBilledUtilityServiceArea()) {
            LOGGER.info("Agreement is billed at a UtilityServiceArea. Finding existing bill in current Quarter....");
            return imaniBillWaterSvcAgreementRepository.getImaniBillForAgreement(waterServiceAgreement.getEmbeddedUtilityService().getUtilityServiceArea(), waterServiceAgreement, billScheduleDate);
        }

        LOGGER.info("No prior saved ImaniBill found for WaterServiceAgreement");
        return Optional.empty();
    }

    private ImaniBill generateImaniBill(WaterServiceAgreement waterServiceAgreement, DateTime billScheduleDate) {
        ImaniBill imaniBill = ImaniBill.builder()
                .amountOwed(0d)
                .amountPaid(0d)
                .billScheduleDate(billScheduleDate)
                .billScheduleTypeE(waterServiceAgreement.getEmbeddedAgreement().getBillScheduleTypeE())
                .billServiceRenderedTypeE(BillServiceRenderedTypeE.Utility_Water)
                .waterServiceAgreement(waterServiceAgreement)
                .build();
        return imaniBill;
    }

}

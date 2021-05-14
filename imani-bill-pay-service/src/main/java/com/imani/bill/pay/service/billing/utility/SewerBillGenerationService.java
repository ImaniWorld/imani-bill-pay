package com.imani.bill.pay.service.billing.utility;

import com.imani.bill.pay.domain.billing.BillScheduleTypeE;
import com.imani.bill.pay.domain.billing.BillServiceRenderedTypeE;
import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.billing.repository.IImaniBillSewerSvcAgreementRepository;
import com.imani.bill.pay.domain.utility.SewerServiceAgreement;
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

import java.util.Optional;

/**
 *
 */
@Service(SewerBillGenerationService.SPRING_BEAN)
public class SewerBillGenerationService implements IBillGenerationService<SewerServiceAgreement> {


    @Autowired
    private IImaniBillSewerSvcAgreementRepository iImaniBillSewerSvcAgreementRepository;
    
    @Autowired
    @Qualifier(DateTimeUtil.SPRING_BEAN)
    private IDateTimeUtil iDateTimeUtil;

    @Autowired
    @Qualifier(UtilityBillingComputeService.SPRING_BEAN)
    private IBillingComputeService iBillingComputeService;

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.billing.utility.SewerBillGenerationService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(SewerBillGenerationService.class);

    @Override
    public boolean generateImaniBill(SewerServiceAgreement sewerServiceAgreement) {
        Assert.notNull(sewerServiceAgreement, "SewerServiceAgreement cannot be null");

        if(sewerServiceAgreement.getEmbeddedAgreement().getBillScheduleTypeE() == BillScheduleTypeE.QUARTERLY) {
            DateTime billScheduleDate = iDateTimeUtil.getDateTimeAtEndOfCurrentQuarter();
            Optional<ImaniBill> imaniBill = getImaniBillForEntity(sewerServiceAgreement, billScheduleDate);

            if(!imaniBill.isPresent()) {
                LOGGER.info("Generating new SewerServiceAgreement ImaniBill in current quarter for Schedule-Date[{}] on {}", billScheduleDate, sewerServiceAgreement.describeAgreement());
                ImaniBill persistedBill = generateImaniBill(sewerServiceAgreement, billScheduleDate);
                iBillingComputeService.computeUpdateBill(sewerServiceAgreement, persistedBill);
            } else {
                // Compute and update all water bills for this agreement
                iBillingComputeService.computeUpdateAgreementBills(sewerServiceAgreement);
            }
        }

        return false;
    }


    private Optional<ImaniBill> getImaniBillForEntity(SewerServiceAgreement sewerServiceAgreement, DateTime billScheduleDate) {
        if(sewerServiceAgreement.getEmbeddedAgreement().isBilledEntityUser()) {
            LOGGER.info("Agreement is billed to User. Finding existing bill in current Quarter....");
            return iImaniBillSewerSvcAgreementRepository.getImaniBillForAgreement(sewerServiceAgreement.getEmbeddedAgreement().getAgreementUserRecord(), sewerServiceAgreement, billScheduleDate);
        } else if(sewerServiceAgreement.getEmbeddedAgreement().isBilledEntityBusiness()) {
            LOGGER.info("Agreement is billed to a Business. Finding existing bill in current Quarter....");
            return iImaniBillSewerSvcAgreementRepository.getImaniBillForAgreement(sewerServiceAgreement.getEmbeddedAgreement().getAgreementBusiness(), sewerServiceAgreement, billScheduleDate);
        } else if(sewerServiceAgreement.getEmbeddedAgreement().isBilledEntityCommunity()) {
            LOGGER.info("Agreement is billed to a Community. Finding existing bill in current Quarter....");
            return iImaniBillSewerSvcAgreementRepository.getImaniBillForAgreement(sewerServiceAgreement.getEmbeddedAgreement().getAgreementCommunity(), sewerServiceAgreement, billScheduleDate);
        } else if(sewerServiceAgreement.getEmbeddedUtilityService().hasBilledUtilityServiceArea()) {
            LOGGER.info("Agreement is billed at a UtilityServiceArea. Finding existing bill in current Quarter....");
            return iImaniBillSewerSvcAgreementRepository.getImaniBillForAgreement(sewerServiceAgreement.getEmbeddedUtilityService().getUtilityServiceArea(), sewerServiceAgreement, billScheduleDate);
        }

        LOGGER.info("No prior saved ImaniBill found for SewerServiceAgreement");
        return Optional.empty();
    }

    private ImaniBill generateImaniBill(SewerServiceAgreement sewerServiceAgreement, DateTime billScheduleDate) {
        ImaniBill imaniBill = ImaniBill.builder()
                .amountOwed(0d)
                .amountPaid(0d)
                .billScheduleDate(billScheduleDate)
                .billScheduleTypeE(sewerServiceAgreement.getEmbeddedAgreement().getBillScheduleTypeE())
                .billServiceRenderedTypeE(BillServiceRenderedTypeE.Utility_Water)
                .sewerServiceAgreement(sewerServiceAgreement)
                .build();
        return imaniBill;
    }

}

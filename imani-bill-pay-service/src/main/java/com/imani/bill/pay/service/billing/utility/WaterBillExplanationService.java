package com.imani.bill.pay.service.billing.utility;

import com.imani.bill.pay.domain.DomainLiteConverterUtil;
import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.billing.ImaniBillExplained;
import com.imani.bill.pay.domain.billing.repository.IImaniBillWaterSvcAgreementRepository;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.utility.*;
import com.imani.bill.pay.domain.utility.repository.IWaterUtilizationChargeRepository;
import com.imani.bill.pay.domain.utility.repository.IWaterUtilizationRepository;
import com.imani.bill.pay.service.billing.IBillExplanationService;
import com.imani.bill.pay.service.util.DateTimeUtil;
import com.imani.bill.pay.service.util.IDateTimeUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service(WaterBillExplanationService.SPRING_BEAN)
public class WaterBillExplanationService implements IBillExplanationService<WaterServiceAgreement>  {



    @Autowired
    @Qualifier(DateTimeUtil.SPRING_BEAN)
    private IDateTimeUtil iDateTimeUtil;

    @Autowired
    private IWaterUtilizationRepository iWaterUtilizationRepository;

    @Autowired
    private IImaniBillWaterSvcAgreementRepository iImaniBillWaterSvcAgreementRepository;

    @Autowired
    private IWaterUtilizationChargeRepository iWaterUtilizationChargeRepository;


    public static final String SPRING_BEAN = "com.imani.bill.pay.service.billing.utility.WaterBillExplanationService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(WaterBillExplanationService.class);



    @Override
    public ExecutionResult<ImaniBillExplained> getCurrentBillExplanation(WaterServiceAgreement waterServiceAgreement) {
        Assert.notNull(waterServiceAgreement, "WaterServiceAgreement cannot be null");
        LOGGER.info("Attempting to generate bill explanation for =>{}", waterServiceAgreement);

        ExecutionResult<ImaniBillExplained> executionResult = new ExecutionResult<>();

        DateTime billScheduleDate = iDateTimeUtil.getDateTimeAStartOfNextQuarter();
        Optional<ImaniBill> imaniBill = iImaniBillWaterSvcAgreementRepository.getImaniBillForAgreement(waterServiceAgreement.getId(), billScheduleDate);
        Optional<ImaniBillExplained> imaniBillExplained = explainImaniBill(imaniBill);

        if(imaniBillExplained.isPresent()) {
            executionResult.setResult(imaniBillExplained.get());

            // Lookup all billing details to form explanation
            // Get the current quarter start date, this will be the scheduled date on the current bill for this agreement
            DateTime atStartOfQuarter = iDateTimeUtil.getDateTimeAStartOfCurrentQuarter();
            DateTime atEndOfQuarter = iDateTimeUtil.getDateTimeAEndOfCurrentQuarter();
            Optional<WaterUtilizationCharge> waterUtilizationCharge = iWaterUtilizationChargeRepository.findByImaniBillInQtr(imaniBill.get(), atStartOfQuarter, atEndOfQuarter);
            if (waterUtilizationCharge.isPresent()) {
                List<WaterUtilization> waterUtilizations = iWaterUtilizationRepository.findUtilizationInPeriod(waterServiceAgreement.getId(), atStartOfQuarter, atEndOfQuarter);
                List<WaterUtilizationLite> waterUtilizationLites = DomainLiteConverterUtil.toWaterUtilizationLites(waterUtilizations);
                WaterBillingDetail waterBillingDetail = WaterBillingDetail.builder()
                        .waterUtilizationChargeLite(waterUtilizationCharge.get().toWaterUtilizationChargeLite())
                        .waterUtilizationLites(waterUtilizationLites)
                        .build();
                imaniBillExplained.get().setBillingDetail(waterBillingDetail);
            }
        }

        return executionResult;
    }

}

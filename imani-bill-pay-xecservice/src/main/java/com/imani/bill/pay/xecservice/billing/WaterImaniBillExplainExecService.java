package com.imani.bill.pay.xecservice.billing;

import com.imani.bill.pay.domain.DomainLiteConverterUtil;
import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.billing.ImaniBillExplained;
import com.imani.bill.pay.domain.billing.repository.IImaniBillRepository;
import com.imani.bill.pay.domain.billing.repository.IImaniBillWaterSvcAgreementRepository;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.utility.*;
import com.imani.bill.pay.domain.utility.repository.IWaterUtilizationChargeRepository;
import com.imani.bill.pay.domain.utility.repository.IWaterUtilizationRepository;
import com.imani.bill.pay.service.util.DateTimeUtil;
import com.imani.bill.pay.service.util.IDateTimeUtil;
import org.apache.commons.lang3.tuple.ImmutablePair;
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
@Service(WaterImaniBillExplainExecService.SPRING_BEAN)
public class WaterImaniBillExplainExecService implements IImaniBillExplainExecService<WaterServiceAgreement> {



    @Autowired
    private IImaniBillRepository imaniBillRepository;

    @Autowired
    private IImaniBillWaterSvcAgreementRepository iImaniBillWaterSvcAgreementRepository;

    @Autowired
    private IWaterUtilizationChargeRepository iWaterUtilizationChargeRepository;


    @Autowired
    @Qualifier(DateTimeUtil.SPRING_BEAN)
    private IDateTimeUtil iDateTimeUtil;

    @Autowired
    private IWaterUtilizationRepository iWaterUtilizationRepository;




    public static final String SPRING_BEAN = "com.imani.bill.pay.xecservice.billing.WaterImaniBillExplainExecService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(WaterImaniBillExplainExecService.class);



    @Override
    public void getCurrentBillExplanation(WaterServiceAgreement agreement, ExecutionResult<ImaniBillExplained> executionResult) {

    }

    @Override
    public void explainImaniBill(Long imaniBillID, ExecutionResult<ImaniBillExplained> executionResult) {
        Assert.notNull(imaniBillID, "ImaniBillID cannot be null");
        Assert.notNull(executionResult, "ExecutionResult cannot be null");

        LOGGER.info("Call #explainImaniBill() validation errors found=> [{}]", executionResult.hasValidationAdvice());

        if(!executionResult.hasValidationAdvice()
                && !executionResult.hasExecutionError()) {
            Optional<ImaniBill> imaniBill = imaniBillRepository.findById(imaniBillID);
            WaterServiceAgreement waterServiceAgreement = imaniBill.get().getWaterServiceAgreement();
            ImaniBillExplained<WaterBillingDetail> imaniBillExplained = imaniBill.get().toImaniBillExplained();

            // Find water utilization details for this bill
            ImmutablePair<DateTime, DateTime> dateTimeImmutablePair = iDateTimeUtil.getQuarterStartEndDates(imaniBill.get().getBillScheduleDate());
            DateTime start = dateTimeImmutablePair.getLeft();
            DateTime end = dateTimeImmutablePair.getRight();
            Optional<WaterUtilizationCharge> waterUtilizationCharge = iWaterUtilizationChargeRepository.findByImaniBillInQtr(imaniBill.get(), start, end);
            List<WaterUtilization> waterUtilizations = iWaterUtilizationRepository.findUtilizationInPeriod(waterServiceAgreement, start, end);
            List<WaterUtilizationLite> waterUtilizationLites = DomainLiteConverterUtil.toWaterUtilizationLites(waterUtilizations);

            // Build WaterBillingDetail for explanation
            WaterBillingDetail waterBillingDetail = WaterBillingDetail.builder()
                    .waterUtilizationChargeLite(waterUtilizationCharge.get().toWaterUtilizationChargeLite())
                    .waterUtilizationLites(waterUtilizationLites)
                    .build();
            imaniBillExplained.setBillingDetail(waterBillingDetail);
            executionResult.setResult(imaniBillExplained);
        }
    }


}

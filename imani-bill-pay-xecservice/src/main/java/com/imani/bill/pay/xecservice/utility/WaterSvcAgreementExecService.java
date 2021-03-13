package com.imani.bill.pay.xecservice.utility;

import com.imani.bill.pay.domain.billing.BillPayFee;
import com.imani.bill.pay.domain.billing.repository.IBillPayFeeRepository;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.utility.WaterServiceAgreement;
import com.imani.bill.pay.domain.utility.WaterUtilization;
import com.imani.bill.pay.service.utility.IWaterSvcAgreementService;
import com.imani.bill.pay.service.utility.IWaterUtilizationService;
import com.imani.bill.pay.service.utility.WaterSvcAgreementService;
import com.imani.bill.pay.service.utility.WaterUtilizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author manyce400
 */
@Service(WaterSvcAgreementExecService.SPRING_BEAN)
public class WaterSvcAgreementExecService implements IWaterSvcAgreementExecService {


    @Autowired
    private IBillPayFeeRepository iBillPayFeeRepository;

    @Autowired
    @Qualifier(WaterSvcAgreementService.SPRING_BEAN)
    private IWaterSvcAgreementService iWaterSvcAgreementService;

    @Autowired
    @Qualifier(WaterUtilizationService.SPRING_BEAN)
    private IWaterUtilizationService iWaterUtilizationService;

    public static final String SPRING_BEAN = "com.imani.bill.pay.xecservice.utility.WaterSvcAgreementExecService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(WaterSvcAgreementExecService.class);


    @Override
    public void processWaterSvcAgreement(ExecutionResult<WaterServiceAgreement> executionResult, List<BillPayFee> billPayFees) {
        if(!executionResult.hasValidationAdvice()) {
            LOGGER.debug("Passed initial Advisor validation, invoking service bean to create agreement...");

            // Load BillPayFee objects
            List<BillPayFee> persistedBillPayFees = new ArrayList<>();
            for(BillPayFee billPayFee : billPayFees) {
                Optional<BillPayFee> persistedBillPayFee = iBillPayFeeRepository.findById(billPayFee.getId());
                if(persistedBillPayFee.isPresent()) {
                    persistedBillPayFees.add(persistedBillPayFee.get());
                }
            }

            iWaterSvcAgreementService.createAgreement(executionResult, persistedBillPayFees);
        }
    }

    @Override
    public void processWaterUtilization(ExecutionResult<WaterUtilization> executionResult) {
        if(!executionResult.hasValidationAdvice()) {
            iWaterUtilizationService.logWaterUtilization(executionResult);
        }
    }
}
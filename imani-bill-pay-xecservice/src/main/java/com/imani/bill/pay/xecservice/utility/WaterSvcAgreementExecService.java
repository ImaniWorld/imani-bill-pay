package com.imani.bill.pay.xecservice.utility;

import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.utility.WaterServiceAgreement;
import com.imani.bill.pay.service.utility.IWaterSvcAgreementService;
import com.imani.bill.pay.service.utility.WaterSvcAgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author manyce400
 */
@Service(WaterSvcAgreementExecService.SPRING_BEAN)
public class WaterSvcAgreementExecService implements IWaterSvcAgreementExecService {


    @Autowired
    @Qualifier(WaterSvcAgreementService.SPRING_BEAN)
    private IWaterSvcAgreementService iWaterSvcAgreementService;

    public static final String SPRING_BEAN = "com.imani.bill.pay.xecservice.utility.WaterSvcAgreementExecService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(WaterSvcAgreementExecService.class);


    @Override
    public void processWaterSvcAgreement(ExecutionResult<WaterServiceAgreement> executionResult) {
        if(!executionResult.hasValidationAdvice()) {
            LOGGER.debug("Passed initial Advisor validation, invoking service bean to create agreement...");
            iWaterSvcAgreementService.createAgreement(executionResult.getResult().get(), executionResult);
        }
    }

}

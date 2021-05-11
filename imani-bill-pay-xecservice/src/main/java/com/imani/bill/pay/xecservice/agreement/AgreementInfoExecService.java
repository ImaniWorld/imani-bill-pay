package com.imani.bill.pay.xecservice.agreement;

import com.imani.bill.pay.domain.DomainLiteConverterUtil;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.utility.WaterServiceAgreement;
import com.imani.bill.pay.domain.utility.WaterServiceAgreementLite;
import com.imani.bill.pay.service.utility.IWaterSvcAgreementService;
import com.imani.bill.pay.service.utility.WaterSvcAgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author manyce400
 */
@Service(AgreementInfoExecService.SPRING_BEAN)
public class AgreementInfoExecService implements IAgreementInfoExecService {


    @Autowired
    @Qualifier(WaterSvcAgreementService.SPRING_BEAN)
    private IWaterSvcAgreementService iWaterSvcAgreementService;

    public static final String SPRING_BEAN = "com.imani.bill.pay.xecservice.agreement.AgreementInfoExecService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(AgreementInfoExecService.class);

    @Override
    public void findCommunityPropertiesWaterAgreements(Long communityID, ExecutionResult<List<WaterServiceAgreementLite>> executionResult) {
        Assert.notNull(communityID, "CommunityID cannot be null");
        Assert.notNull(executionResult, "executionResult cannot be null");

        LOGGER.info("Call #findCommunityPropertiesWaterAgreements() validation errors found=> [{}]", executionResult.hasExecutionError());

        if(!executionResult.hasValidationAdvice()
                && !executionResult.hasExecutionError()) {
            List<WaterServiceAgreement> waterServiceAgreements = iWaterSvcAgreementService.findCommunityPropertiesWaterSvcAgreements(communityID);
            List<WaterServiceAgreementLite> waterServiceAgreementLites = DomainLiteConverterUtil.toLite(waterServiceAgreements);
            executionResult.setResult(waterServiceAgreementLites);
        }
    }

}
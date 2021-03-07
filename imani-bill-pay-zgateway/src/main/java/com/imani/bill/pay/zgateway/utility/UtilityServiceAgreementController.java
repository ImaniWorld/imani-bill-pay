package com.imani.bill.pay.zgateway.utility;

import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.gateway.APIGatewayRequest;
import com.imani.bill.pay.domain.gateway.APIGatewayResponse;
import com.imani.bill.pay.domain.utility.SewerServiceAgreement;
import com.imani.bill.pay.domain.utility.WaterServiceAgreement;
import com.imani.bill.pay.xecservice.utility.ISewerSvcAgreementExecService;
import com.imani.bill.pay.xecservice.utility.IWaterSvcAgreementExecService;
import com.imani.bill.pay.xecservice.utility.SewerSvcAgreementExecService;
import com.imani.bill.pay.xecservice.utility.WaterSvcAgreementExecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author manyce400
 */
@RestController
@RequestMapping("/business/utility/agreement")
public class UtilityServiceAgreementController {


    @Autowired
    @Qualifier(WaterSvcAgreementExecService.SPRING_BEAN)
    private IWaterSvcAgreementExecService iWaterSvcAgreementExecService;

    @Autowired
    @Qualifier(SewerSvcAgreementExecService.SPRING_BEAN)
    private ISewerSvcAgreementExecService iSewerSvcAgreementExecService;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(UtilityServiceAgreementController.class);

    @PostMapping("/new/water")
    public APIGatewayResponse newWaterSvcAgreement(@RequestBody APIGatewayRequest<WaterServiceAgreement> apiGatewayRequest) {
        LOGGER.info("Received a request to create a new Water Service Agreement.");
        ExecutionResult<WaterServiceAgreement> executionResult = new ExecutionResult<>(apiGatewayRequest.getRequestObject());
        iWaterSvcAgreementExecService.processWaterSvcAgreement(executionResult, apiGatewayRequest.getBillPayFees());
        return APIGatewayResponse.fromExecutionResult(executionResult);
    }

    @PostMapping("/new/sewer")
    public APIGatewayResponse newSewerSvcAgreement(@RequestBody APIGatewayRequest<SewerServiceAgreement> apiGatewayRequest) {
        LOGGER.info("Received a request to create a new Sewer Service Agreement.");
        ExecutionResult<SewerServiceAgreement> executionResult = new ExecutionResult<>(apiGatewayRequest.getRequestObject());
        iSewerSvcAgreementExecService.processSewerSvcAgreement(executionResult);
        return APIGatewayResponse.fromExecutionResult(executionResult);
    }

}
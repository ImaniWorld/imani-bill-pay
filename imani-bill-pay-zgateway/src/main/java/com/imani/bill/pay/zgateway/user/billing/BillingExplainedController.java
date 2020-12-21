package com.imani.bill.pay.zgateway.user.billing;

import com.imani.bill.pay.domain.billing.ImaniBillExplained;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.gateway.APIGatewayRequest;
import com.imani.bill.pay.domain.gateway.APIGatewayResponse;
import com.imani.bill.pay.service.billing.IBillExplanationService;
import com.imani.bill.pay.service.billing.ResidentialPropertyLeaseBillExplanationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author manyce400
 */
@RestController
@RequestMapping("/user/billing/explained")
public class BillingExplainedController {


    @Autowired
    @Qualifier(ResidentialPropertyLeaseBillExplanationService.SPRING_BEAN)
    private IBillExplanationService iBillExplanationService;


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(BillingExplainedController.class);


    @PostMapping("/lease/current")
    public APIGatewayResponse getCurrentResidentialLeaseBill(@RequestBody APIGatewayRequest apiGatewayRequest) {
        LOGGER.info("Generating current month lease agreement bill for user:=> {}", apiGatewayRequest.getUserRecordLite().getEmail());
        ExecutionResult<ImaniBillExplained> executionResult = iBillExplanationService.getCurrentBillExplanation(apiGatewayRequest.getUserRecordLite());
        return APIGatewayResponse.fromExecutionResult(executionResult);
    }


    @PostMapping("/lease/ytd")
    public APIGatewayResponse getYTDResidentialLeaseBills(@RequestBody APIGatewayRequest apiGatewayRequest) {
        LOGGER.info("Generating all YTD residential property lease agreement bills for user:=> {}", apiGatewayRequest.getUserRecordLite().getEmail());
        ExecutionResult<List<ImaniBillExplained>> executionResult = iBillExplanationService.getYTDBillsExplanation(apiGatewayRequest.getUserRecordLite());
        return APIGatewayResponse.fromExecutionResult(executionResult);
    }


}

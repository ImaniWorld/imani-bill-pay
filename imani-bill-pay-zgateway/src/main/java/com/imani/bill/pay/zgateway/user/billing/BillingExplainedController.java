package com.imani.bill.pay.zgateway.user.billing;

import com.imani.bill.pay.domain.billing.ImaniBillExplained;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.gateway.APIGatewayRequest;
import com.imani.bill.pay.domain.gateway.APIGatewayResponse;
import com.imani.bill.pay.service.billing.IBillExplanationService;
import com.imani.bill.pay.service.billing.ResidentialPropertyLeaseBillExplanationService;
import com.imani.bill.pay.service.billing.education.TuitionBillExplanationService;
import com.imani.bill.pay.xecservice.billing.IImaniBillExplainExecService;
import com.imani.bill.pay.xecservice.billing.WaterImaniBillExplainExecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author manyce400
 */
@RestController
@RequestMapping("/user/billing/explained")
public class BillingExplainedController {


    @Autowired
    @Qualifier(ResidentialPropertyLeaseBillExplanationService.SPRING_BEAN)
    private IBillExplanationService residentialBillExplanationService;

    @Autowired
    @Qualifier(TuitionBillExplanationService.SPRING_BEAN)
    private IBillExplanationService tuitionBillExplanationService;

    @Autowired
    @Qualifier(WaterImaniBillExplainExecService.SPRING_BEAN)
    private IImaniBillExplainExecService waterIImaniBillExplainExecService;


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(BillingExplainedController.class);


    @PostMapping("/lease/current")
    public APIGatewayResponse getCurrentResidentialLeaseBill(@RequestBody APIGatewayRequest apiGatewayRequest) {
        LOGGER.info("Generating current month lease agreement bill for user:=> {}", apiGatewayRequest.getUserRecordLite().getEmail());
        ExecutionResult<ImaniBillExplained> executionResult = residentialBillExplanationService.getCurrentBillExplanation(apiGatewayRequest.getUserRecordLite());
        return APIGatewayResponse.fromExecutionResult(executionResult);
    }

    @PostMapping("/tuition/current")
    public APIGatewayResponse getCurrentTuitionBill(@RequestBody APIGatewayRequest apiGatewayRequest) {
        ExecutionResult<ImaniBillExplained> executionResult = tuitionBillExplanationService.getCurrentBillExplanation(apiGatewayRequest.getUserRecordLite());
        return APIGatewayResponse.fromExecutionResult(executionResult);
    }

//    @PostMapping("/water/current")
//    public APIGatewayResponse getCurrentWaterBill(@RequestBody APIGatewayRequest<WaterServiceAgreement> apiGatewayRequest) {
//        ExecutionResult<ImaniBillExplained> executionResult = waterImaniBillExplanationService.getCurrentBillExplanation(apiGatewayRequest.getRequestObject());
//        return APIGatewayResponse.fromExecutionResult(executionResult);
//    }

    @RequestMapping(value = "/water/by/bill/{bill_id}", method = RequestMethod.GET)
    public APIGatewayResponse getUserWaterSvcAgreements(@PathVariable(value="bill_id") Long imaniBillID) {
        LOGGER.info("Received bill explanation request on ImaniBill[ID: {}]", imaniBillID);
        ExecutionResult<ImaniBillExplained> executionResult = new ExecutionResult<>();
        waterIImaniBillExplainExecService.explainImaniBill(imaniBillID, executionResult);
        return APIGatewayResponse.fromExecutionResult(executionResult);
    }


    @PostMapping("/lease/ytd")
    public APIGatewayResponse getYTDResidentialLeaseBills(@RequestBody APIGatewayRequest apiGatewayRequest) {
        LOGGER.info("Generating all YTD residential property lease agreement bills for user:=> {}", apiGatewayRequest.getUserRecordLite().getEmail());
        ExecutionResult<List<ImaniBillExplained>> executionResult = new ExecutionResult<>();
        return APIGatewayResponse.fromExecutionResult(executionResult);
    }


}

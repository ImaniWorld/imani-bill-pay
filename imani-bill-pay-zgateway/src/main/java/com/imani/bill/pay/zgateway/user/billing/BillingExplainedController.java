package com.imani.bill.pay.zgateway.user.billing;

import com.imani.bill.pay.domain.billing.ImaniBillExplained;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.execution.ValidationAdvice;
import com.imani.bill.pay.domain.gateway.APIGatewayRequest;
import com.imani.bill.pay.domain.gateway.APIGatewayResponse;
import com.imani.bill.pay.domain.property.LeaseAgreementLite;
import com.imani.bill.pay.service.billing.IBillExplanationService;
import com.imani.bill.pay.service.billing.ResidentialPropertyLeaseBillExplanationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

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
    public APIGatewayResponse getUserLeaseAgreementBill(@RequestBody APIGatewayRequest<LeaseAgreementLite> apiGatewayRequest) {
        LOGGER.info("Generating current month lease agreement bill for user:=> {}", apiGatewayRequest.getUserRecordLite().getEmail());

        Optional<ImaniBillExplained> imaniBillExplained = iBillExplanationService.getCurrentBillExplanation(apiGatewayRequest.getUserRecordLite());
        ExecutionResult<ImaniBillExplained> executionResult = new ExecutionResult<>();
        if(!imaniBillExplained.isPresent()) {
            executionResult.addValidationAdvice(ValidationAdvice.newInstance("No lease agreement bill found for user"));
        } else {
            executionResult.setResult(imaniBillExplained.get());
        }

        return APIGatewayResponse.fromExecutionResult(executionResult);
    }




}

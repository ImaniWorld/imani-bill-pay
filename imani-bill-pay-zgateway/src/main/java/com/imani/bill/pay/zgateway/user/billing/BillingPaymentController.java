package com.imani.bill.pay.zgateway.user.billing;

import com.imani.bill.pay.domain.billing.ImaniBillExplained;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.gateway.APIGatewayRequest;
import com.imani.bill.pay.domain.gateway.APIGatewayResponse;
import com.imani.bill.pay.service.billing.collection.BillPayCollectionService;
import com.imani.bill.pay.service.billing.collection.IBillPayCollectionService;
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
@RequestMapping("/user/billing/payment")
public class BillingPaymentController {


    @Autowired
    @Qualifier(BillPayCollectionService.SPRING_BEAN)
    private IBillPayCollectionService iBillPayCollectionService;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(BillingPaymentController.class);

    @PostMapping("/residential/lease")
    public APIGatewayResponse getUserLeaseAgreementBill(@RequestBody APIGatewayRequest<ImaniBillExplained> apiGatewayRequest) {
        LOGGER.info("Processing a residential lease bill payment....");
        ExecutionResult<ImaniBillExplained> executionResult = iBillPayCollectionService.collectPayment(apiGatewayRequest.getRequestObject());
        return APIGatewayResponse.fromExecutionResult(executionResult);
    }
}

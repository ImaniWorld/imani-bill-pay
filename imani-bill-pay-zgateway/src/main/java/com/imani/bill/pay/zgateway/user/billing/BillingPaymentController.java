package com.imani.bill.pay.zgateway.user.billing;

import com.imani.bill.pay.domain.billing.ImaniBillExplained;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.gateway.APIGatewayRequest;
import com.imani.bill.pay.domain.gateway.APIGatewayResponse;
import com.imani.bill.pay.domain.payment.record.ImaniBillPayRecord;
import com.imani.bill.pay.xecservice.collection.BillPayCollectService;
import com.imani.bill.pay.xecservice.collection.IBillPayCollectService;
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
    @Qualifier(BillPayCollectService.SPRING_BEAN)
    private IBillPayCollectService iBillPayCollectService;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(BillingPaymentController.class);

    @PostMapping("/process")
    public APIGatewayResponse getUserLeaseAgreementBill(@RequestBody APIGatewayRequest<ImaniBillExplained> apiGatewayRequest) {
        LOGGER.info("Received a request to process Imani Bill payment....");
        ImaniBillPayRecord imaniBillPayRecord = ImaniBillPayRecord.builder()
                .build();
        ExecutionResult<ImaniBillExplained> executionResult = new ExecutionResult<>(apiGatewayRequest.getRequestObject());
        iBillPayCollectService.processPayment(imaniBillPayRecord, executionResult);
        return APIGatewayResponse.fromExecutionResult(executionResult);
    }
}

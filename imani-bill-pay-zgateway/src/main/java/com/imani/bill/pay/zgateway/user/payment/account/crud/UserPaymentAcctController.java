package com.imani.bill.pay.zgateway.user.payment.account.crud;

import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.gateway.APIGatewayRequest;
import com.imani.bill.pay.domain.gateway.APIGatewayResponse;
import com.imani.bill.pay.service.payment.stripe.IStripeCustomerService;
import com.imani.bill.pay.service.payment.stripe.StripeCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller exposes all critical APIs for creating Imani BillPay tenant payment account details and information.
 *
 * @author manyce400
 */
@RestController
@RequestMapping("/user/payment/account")
public class UserPaymentAcctController {


    @Autowired
    @Qualifier(StripeCustomerService.SPRING_BEAN)
    private IStripeCustomerService iStripeCustomerService;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(UserPaymentAcctController.class);


    /**
     * Create Stripe Customer account for Imani BillPay user. Stripe Customer account will be used for all payments made by user.
     *
     * @param apiGatewayRequest
     * @return
     */
    @PostMapping("/stripe/create")
    public APIGatewayResponse createStripeAccount(@RequestBody APIGatewayRequest apiGatewayRequest) {
        LOGGER.info("Attempting to create a Stripe payment account for user in request:=> {}", apiGatewayRequest.getOnBehalfOf());
        ExecutionResult executionResult = iStripeCustomerService.createPlaidStripeCustomerBankAcct(apiGatewayRequest.getOnBehalfOf());
        return APIGatewayResponse.fromExecutionResult(executionResult);
    }


}

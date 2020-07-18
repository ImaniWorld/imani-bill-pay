package com.imani.bill.pay.zgateway.payment;

import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.gateway.APIGatewayRequest;
import com.imani.bill.pay.domain.gateway.APIGatewayResponse;
import com.imani.bill.pay.domain.payment.plaid.gateway.PlaidAcctLinkRequest;
import com.imani.bill.pay.service.payment.plaid.IPlaidAccountMasterService;
import com.imani.bill.pay.service.payment.plaid.PlaidAccountMasterService;
import com.imani.bill.pay.service.payment.stripe.IStripeCustomerService;
import com.imani.bill.pay.service.payment.stripe.StripeCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

/**
 * @author manyce400
 */
@RestController
public class PlaidStripeController {



    @Autowired
    @Qualifier(PlaidAccountMasterService.SPRING_BEAN)
    private IPlaidAccountMasterService iPlaidAccountMasterService;


    @Autowired
    @Qualifier(StripeCustomerService.SPRING_BEAN)
    private IStripeCustomerService iStripeCustomerService;


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PlaidStripeController.class);



    @PostMapping("/link/plaid/account")
    public APIGatewayResponse linkPlaidAccount(@RequestBody PlaidAcctLinkRequest plaidAcctLinkRequest) {
        LOGGER.info("Attempting to link Plaid account to existing user=> {}", plaidAcctLinkRequest);
        ExecutionResult executionResult = iPlaidAccountMasterService.linkPlaidBankAcct(plaidAcctLinkRequest.getPlaidPublicToken(), plaidAcctLinkRequest.getOnBehalfOf());
        return APIGatewayResponse.fromExecutionResult(executionResult);
    }


    @PostMapping("/link/plaid/stripe")
    public APIGatewayResponse linkPlaidAccount(@RequestBody APIGatewayRequest apiGatewayRequest) {
        LOGGER.info("Attempting to link Stripe account for user in request:=> {}", apiGatewayRequest);
        ExecutionResult executionResult = iPlaidAccountMasterService.createStripeAcctForPrimaryPlaidAcct(apiGatewayRequest.getOnBehalfOf());
        return APIGatewayResponse.fromExecutionResult(executionResult);
    }


    @PostMapping("/stripe/account/create")
    public APIGatewayResponse createStripeAccount(@RequestBody APIGatewayRequest apiGatewayRequest) {
        LOGGER.info("Attempting to update Stripe account for user in request:=> {}", apiGatewayRequest);
        ExecutionResult executionResult = iStripeCustomerService.createPlaidStripeCustomerBankAcct(apiGatewayRequest.getOnBehalfOf());
        return APIGatewayResponse.fromExecutionResult(executionResult);
    }

    @PostMapping("/stripe/account/update")
    public APIGatewayResponse updateStripeAccount(@RequestBody APIGatewayRequest apiGatewayRequest) {
        LOGGER.info("Attempting to update Stripe account for user in request:=> {}", apiGatewayRequest);
        ExecutionResult executionResult = iStripeCustomerService.updatePrimaryStripeCustomerBankAcct(apiGatewayRequest.getOnBehalfOf());
        return APIGatewayResponse.fromExecutionResult(executionResult);
    }

}

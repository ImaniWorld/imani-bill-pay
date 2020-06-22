package com.imani.bill.pay.zgateway.user.payment.account.crud;

import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.gateway.APIGatewayRequest;
import com.imani.bill.pay.domain.gateway.APIGatewayResponse;
import com.imani.bill.pay.service.payment.plaid.IPlaidAccountMasterService;
import com.imani.bill.pay.service.payment.plaid.PlaidAccountMasterService;
import com.imani.bill.pay.service.payment.stripe.IStripeCustomerService;
import com.imani.bill.pay.service.payment.stripe.StripeCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    @Qualifier(PlaidAccountMasterService.SPRING_BEAN)
    private IPlaidAccountMasterService iPlaidAccountMasterService;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(UserPaymentAcctController.class);


    /**
     * Step 0:
     * This API will be invoked directly from Imani BillPay integration.html after client successfully logs into their bank account using Plaid.
     * A public_token and accountID is returned which we will have to save to be able to perform additional actions with Plaid on that account
     */
    @GetMapping("/register/plaid/{publicToken}/{acctID}/{userID}")
    public APIGatewayResponse registerAuthenticatedPlaidAccount(@PathVariable("publicToken") String publicToken,
                                                  @PathVariable("acctID") String accountID, @PathVariable("userID") String userID) {
        LOGGER.info("Received call to register new Plaid Bank account with PublicToken:=> {} accountID:=> {} for userID:=> {}", publicToken, accountID, userID);
        ExecutionResult executionResult = iPlaidAccountMasterService.linkPlaidBankAcct(publicToken, userID);
        return APIGatewayResponse.fromExecutionResult(executionResult);
    }


    /**
     * Step 1:
     * After Imani BillPay user has logged into their bank account via Plaid, Plaid account access tokens and details are persisted
     */
    @PostMapping("/link/plaid/stripe")
    public APIGatewayResponse linkPlaidAccount(@RequestBody APIGatewayRequest apiGatewayRequest) {
        LOGGER.info("Attempting to link Stripe account for user in request:=> {}", apiGatewayRequest);
        ExecutionResult executionResult = iPlaidAccountMasterService.createStripeAcctForPrimaryPlaidAcct(apiGatewayRequest.getOnBehalfOf());
        return APIGatewayResponse.fromExecutionResult(executionResult);
    }


    /**
     * Step 2:
     * Create Stripe Customer account for Imani BillPay user. Stripe Customer account will be used for all payments made by user.
     * Expectations, this API should only be called once we have used Plaid to validate a user's bank account after they have entered credentials
     * and a valid Plaid Stripe bank account token has been captured and properly saved in ACHPaymentInfo
     */
    @PostMapping("/stripe/create")
    public APIGatewayResponse createStripeAccount(@RequestBody APIGatewayRequest apiGatewayRequest) {
        LOGGER.info("Attempting to create a Stripe payment account for user in request:=> {}", apiGatewayRequest.getOnBehalfOf());
        ExecutionResult executionResult = iStripeCustomerService.createPlaidStripeCustomerBankAcct(apiGatewayRequest.getOnBehalfOf());
        return APIGatewayResponse.fromExecutionResult(executionResult);
    }


}

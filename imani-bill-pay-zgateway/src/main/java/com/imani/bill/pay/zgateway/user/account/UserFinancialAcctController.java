package com.imani.bill.pay.zgateway.user.account;

import com.imani.bill.pay.domain.gateway.APIGatewayEvent;
import com.imani.bill.pay.domain.gateway.APIGatewayResponse;
import com.imani.bill.pay.domain.gateway.GenericAPIGatewayRequest;
import com.imani.bill.pay.domain.gateway.GenericAPIGatewayResponse;
import com.imani.bill.pay.service.payment.stripe.IStripeCustomerService;
import com.imani.bill.pay.service.payment.stripe.StripeCustomerService;
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
@RequestMapping("/user/financial/account")
public class UserFinancialAcctController {


    @Autowired
    @Qualifier(StripeCustomerService.SPRING_BEAN)
    private IStripeCustomerService iStripeCustomerService;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(UserFinancialAcctController.class);



    @PostMapping("/link/stripe")
    public APIGatewayResponse linkStripeAcct(@RequestBody APIGatewayEvent<GenericAPIGatewayRequest, GenericAPIGatewayResponse> apiGatewayEvent) {
        LOGGER.info("Attempting to build Stripe and link Stripe account to accociated Plaid account for user: {}", apiGatewayEvent.getRequestBody().get().getExecUserRecord().get().getEmbeddedContactInfo().getEmail());
//        Optional<ACHPaymentInfo> achPaymentInfo = iStripeCustomerService.createPlaidStripeCustomerBankAcct(apiGatewayEvent.getRequestBody().get().getExecUserRecord().get(), "public-sandbox-ed01cd78-58f2-489a-8137-b3187d26018a", "5vdBElbnN5HRP5pPbJnLHlllmmqrvBuZ1pB8N");
//        System.out.println("achPaymentInfo = " + achPaymentInfo);
        return null;
    }
}

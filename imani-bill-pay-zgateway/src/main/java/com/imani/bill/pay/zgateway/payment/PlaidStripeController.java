package com.imani.bill.pay.zgateway.payment;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author manyce400
 */
@RestController
public class PlaidStripeController {


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PlaidStripeController.class);

    @GetMapping("/register/plaid/account/{publicToken}/{acctID}")
    public void registerAuthenticatedPlaidAccount(@PathVariable("publicToken") String publicToken, @PathVariable("acctID") String accountID) {
        LOGGER.info("Received call to register new Plaid Bank account with PublicToken:=> {} accountID :=> {}", publicToken, accountID);
    }
}

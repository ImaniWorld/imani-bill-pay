package com.imani.bill.pay.zgateway.payment;

import com.imani.bill.pay.domain.payment.plaid.gateway.PlaidAcctLinkRequest;
import com.imani.bill.pay.service.payment.plaid.IPlaidAccountMasterService;
import com.imani.bill.pay.service.payment.plaid.PlaidAccountMasterService;
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

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PlaidStripeController.class);

    @GetMapping("/register/plaid/account/{publicToken}/{acctID}")
    public void registerAuthenticatedPlaidAccount(@PathVariable("publicToken") String publicToken, @PathVariable("acctID") String accountID) {
        LOGGER.info("Received call to register new Plaid Bank account with PublicToken:=> {} accountID :=> {}", publicToken, accountID);
    }


    @PostMapping("/link/plaid/account")
    public void linkPlaidAccount(@RequestBody PlaidAcctLinkRequest plaidAcctLinkRequest) {
        LOGGER.info("Attempting to link Plaid account to existing user=> {}", plaidAcctLinkRequest);
        iPlaidAccountMasterService.linkPlaidBankAcct(plaidAcctLinkRequest.getPlaidPublicToken(), plaidAcctLinkRequest.getPlaidAccountID(), plaidAcctLinkRequest.getExecUserRecord().get());
    }




}

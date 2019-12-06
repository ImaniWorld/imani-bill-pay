package com.imani.bill.pay.zgateway.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author manyce400
 */
@Controller
public class PlaidStripeIntegrationController {


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PlaidStripeIntegrationController.class);

    @GetMapping("/stripe/plaid/integration")
    public String displayIntegration(Model model) {
        LOGGER.info("Building Stripe and Plaid Integration web page....");
        return "integration";
    }
}

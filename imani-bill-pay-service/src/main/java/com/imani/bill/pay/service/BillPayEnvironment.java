package com.imani.bill.pay.service;

import com.imani.bill.pay.domain.payment.config.PlaidAPIConfig;
import com.imani.bill.pay.domain.payment.config.StripeAPIConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class BillPayEnvironment {


    @Autowired
    private PlaidAPIConfig plaidAPIConfig;

    @Autowired
    private StripeAPIConfig stripeAPIConfig;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(BillPayEnvironment.class);


    @PostConstruct
    public void postConstruct() {
        LOGGER.info("===================  Imani BillPay Runtime Environment =======================");
        LOGGER.info("Plaid API Environment => {}", plaidAPIConfig.toString());
        LOGGER.info("Stripe API Environment => {}", stripeAPIConfig.toString());
        LOGGER.info("=================================================================================");
    }
}

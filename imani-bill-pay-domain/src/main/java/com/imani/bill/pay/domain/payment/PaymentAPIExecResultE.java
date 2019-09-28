package com.imani.bill.pay.domain.payment;

/**
 * Tracks the status when a payment API example, Plaid and Stripe is invoked.
 *
 * @author manyce400
 */
public enum PaymentAPIExecResultE {


    Success,

    Failed,

    NoResponse
    ;
}

package com.imani.bill.pay.domain.payment.stripe;

import org.springframework.util.Assert;

/**
 * Stripe BankAccount API Doc: https://stripe.com/docs/api/customer_bank_accounts/object
 *
 * @author manyce400
 */
public enum StripeBankAcctStatusE {


    New("new"),

    Validated("validated"),

    Verified("verified"),

    VerificationFailed("verification_failed"),

    Errored("errored")

    ;

    private String status;

    StripeBankAcctStatusE(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static StripeBankAcctStatusE getByStatus(String status) {
        Assert.notNull(status, "status cannot be null");

        for(StripeBankAcctStatusE stripeBankAcctStatusE : values()) {
            if(stripeBankAcctStatusE.getStatus().equals(status)) {
                return stripeBankAcctStatusE;
            }
        }

        return null;
    }

}

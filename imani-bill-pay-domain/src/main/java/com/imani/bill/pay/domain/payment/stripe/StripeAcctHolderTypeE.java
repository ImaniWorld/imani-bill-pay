package com.imani.bill.pay.domain.payment.stripe;

import java.util.Optional;

/**
 * Stripe BankAccount API Doc: https://stripe.com/docs/api/customer_bank_accounts/object
 *
 * @author manyce400
 */
public enum StripeAcctHolderTypeE {


    Individual("individual"),

    Company("company")
    ;

    private String holderType;

    StripeAcctHolderTypeE(String holderType) {
        this.holderType = holderType;
    }

    public String getHolderType() {
        return holderType;
    }

    public static Optional<StripeAcctHolderTypeE> getByHolderType(String holderType) {
        if (holderType != null) {
            for(StripeAcctHolderTypeE stripeAcctHolderTypeE : values()) {
                if(stripeAcctHolderTypeE.getHolderType().equals(holderType)) {
                    return Optional.of(stripeAcctHolderTypeE);
                }
            }
        }

        return Optional.empty();
    }

}

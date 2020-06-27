package com.imani.bill.pay.domain.payment.plaid;

/**
 * @author manyce400
 */
public enum PlaidAPIInvocationE {

    // Used as part of setup to exchange public token for acess token
    AccessToken,

    AccountsInfo,

    StripeAcctCreate

    ;

}

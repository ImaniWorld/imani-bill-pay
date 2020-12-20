package com.imani.bill.pay.domain.payment;

/**
 * @author manyce400
 */
public enum PaymentStatusE {


    Pending,

    Success,

    Failed,

    Balance_Validation_Fail,

    InsufficientFunds,

    CannotProcess
    ;
}
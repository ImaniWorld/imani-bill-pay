package com.imani.bill.pay.domain.payment.stripe;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Domain object for reflecting all properties of a Stripe BankAccount.
 * Stripe BankAccount API Doc: https://stripe.com/docs/api/customer_bank_accounts/object
 *
 * @author manyce400
 */
@Embeddable
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StripeBankAcct {



    @Column(name="StripeBankAcctID", nullable=true, length=200)
    private String id;

    @Column(name="StripeObject", nullable=true, length=200)
    private String object;

    @Column(name="StripeAccountHolderName", nullable=true, length=200)
    private String accountHolderName;

    @Column(name="StripeAccountHolderType", nullable=true, length=100)
    @Enumerated(EnumType.STRING)
    private StripeAcctHolderTypeE accountHolderType;

    @Column(name="StripeBankName", nullable=true, length=200)
    private String bankName;

    @Column(name="StripeBankCountry", nullable=true, length=100)
    private String country;

    @Column(name="StripeBankCurrency", nullable=true, length=10)
    private String currency;

    @Column(name="StripeBankAcctLast4Digits", nullable=true, length=10)
    private String last4;

    @Column(name="StripeBankRoutingNumber", nullable=true, length=10)
    private String routingNumber;

    @Column(name="StripeBankAcctStatus", nullable=true, length=15)
    @Enumerated(EnumType.STRING)
    private StripeBankAcctStatusE acctStatus;



    public StripeBankAcct() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public StripeAcctHolderTypeE getAccountHolderType() {
        return accountHolderType;
    }

    public void setAccountHolderType(StripeAcctHolderTypeE accountHolderType) {
        this.accountHolderType = accountHolderType;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLast4() {
        return last4;
    }

    public void setLast4(String last4) {
        this.last4 = last4;
    }

    public String getRoutingNumber() {
        return routingNumber;
    }

    public void setRoutingNumber(String routingNumber) {
        this.routingNumber = routingNumber;
    }

    public StripeBankAcctStatusE getAcctStatus() {
        return acctStatus;
    }

    public void setAcctStatus(StripeBankAcctStatusE acctStatus) {
        this.acctStatus = acctStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("object", object)
                .append("accountHolderName", accountHolderName)
                .append("accountHolderType", accountHolderType)
                .append("bankName", bankName)
                .append("country", country)
                .append("currency", currency)
                .append("last4", last4)
                .append("routingNumber", routingNumber)
                .append("acctStatus", acctStatus)
                .toString();
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private StripeBankAcct stripeBankAcct = new StripeBankAcct();

        public Builder id(String id) {
            stripeBankAcct.id = id;
            return this;
        }

        public Builder object(String object) {
            stripeBankAcct.object = object;
            return this;
        }

        public Builder accountHolderName(String accountHolderName) {
            stripeBankAcct.accountHolderName = accountHolderName;
            return this;
        }

        public Builder accountHolderType(StripeAcctHolderTypeE acctHolderType) {
            stripeBankAcct.accountHolderType = acctHolderType;
            return this;
        }

        public Builder bankName(String bankName) {
            stripeBankAcct.bankName = bankName;
            return this;
        }

        public Builder country(String country) {
            stripeBankAcct.country = country;
            return this;
        }

        public Builder currency(String currency) {
            stripeBankAcct.currency = currency;
            return this;
        }

        public Builder last4(String last4) {
            stripeBankAcct.last4 = last4;
            return this;
        }

        public Builder routingNumber(String routingNumber) {
            stripeBankAcct.routingNumber = routingNumber;
            return this;
        }

        public Builder acctStatus(StripeBankAcctStatusE acctStatus) {
            stripeBankAcct.acctStatus = acctStatus;
            return this;
        }

        public StripeBankAcct build() {
            return stripeBankAcct;
        }
    }

}

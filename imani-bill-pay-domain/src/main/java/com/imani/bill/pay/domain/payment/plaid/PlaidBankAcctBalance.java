package com.imani.bill.pay.domain.payment.plaid;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.imani.bill.pay.domain.AuditableRecord;
import com.imani.bill.pay.domain.payment.ACHPaymentInfo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

import javax.persistence.*;

/**
 * Tracks Bank account balances of linked Plaid Bank Account's for ACH payment purposes.
 *
 * @author manyce400
 */
@Entity
@Table(name="PlaidBankAcctBalance")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaidBankAcctBalance extends AuditableRecord {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;


    // Tracks the available balance on the account
    @Column(name="Available", nullable=true)
    private Double available;


    // Tracks the current balance on the account
    @Column(name="Current", nullable=true)
    private Double current;


    // Tracks the limit of the account
    @Column(name="AcctLimit", nullable=true)
    private Double limit;


    // Tracks the currency of the account, we dont expect this to change during the lifetime of the account
    @Column(name="CurrencyCode", nullable=true)
    @JsonProperty("iso_currency_code")
    private String currencyCode;


    // This should be the same as the Currency Code but expanded.
    @Column(name="UnOfficialCurrency", nullable=true)
    @JsonProperty("unofficial_currency_code")
    private String unOfficialCurrency;


    // Tracks the ACHPaymentInfo/Plaid Bank account that this balance belongs to.  This should never be null
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACHPaymentInfoID")
    private ACHPaymentInfo achPaymentInfo;


    public PlaidBankAcctBalance() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAvailable() {
        return available;
    }

    public void setAvailable(Double available) {
        this.available = available;
    }

    public Double getCurrent() {
        return current;
    }

    public void setCurrent(Double current) {
        this.current = current;
    }

    public Double getLimit() {
        return limit;
    }

    public void setLimit(Double limit) {
        this.limit = limit;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getUnOfficialCurrency() {
        return unOfficialCurrency;
    }

    public void setUnOfficialCurrency(String unOfficialCurrency) {
        this.unOfficialCurrency = unOfficialCurrency;
    }

    public ACHPaymentInfo getAchPaymentInfo() {
        return achPaymentInfo;
    }

    public void setAchPaymentInfo(ACHPaymentInfo achPaymentInfo) {
        this.achPaymentInfo = achPaymentInfo;
    }

    public boolean hasAvailableBalanceForPayment(Double paymentAmount) {
        Assert.notNull(paymentAmount, "paymentAmount cannot be null");
        Assert.isTrue(paymentAmount.doubleValue() > 0, "paymentAmount cannot be 0");
        return available.doubleValue() > paymentAmount.doubleValue();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("available", available)
                .append("current", current)
                .append("limit", limit)
                .append("currencyCode", currencyCode)
                .append("unOfficialCurrency", unOfficialCurrency)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private PlaidBankAcctBalance plaidBankAcctBalance = new PlaidBankAcctBalance();

        public Builder available(Double available) {
            plaidBankAcctBalance.available = available;
            return this;
        }

        public Builder current(Double current) {
            plaidBankAcctBalance.current = current;
            return this;
        }

        public Builder limit(Double limit) {
            plaidBankAcctBalance.limit = limit;
            return this;
        }

        public Builder currencyCode(String currencyCode) {
            plaidBankAcctBalance.currencyCode = currencyCode;
            return this;
        }

        public Builder unOfficialCurrency(String unOfficialCurrency) {
            plaidBankAcctBalance.unOfficialCurrency = unOfficialCurrency;
            return this;
        }

        public Builder achPaymentInfo(ACHPaymentInfo achPaymentInfo) {
            plaidBankAcctBalance.achPaymentInfo = achPaymentInfo;
            return this;
        }

        public PlaidBankAcctBalance build() {
            return plaidBankAcctBalance;
        }
    }

}

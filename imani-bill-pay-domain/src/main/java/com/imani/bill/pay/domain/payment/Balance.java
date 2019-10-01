package com.imani.bill.pay.domain.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

/**
 * Tracks Bank account balances using Plaid integration model.
 *
 * @author manyce400
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Balance {


    private Double available;

    private Double current;

    private Double limit;

    @JsonProperty("iso_currency_code")
    private String currencyCode;

    @JsonProperty("unofficial_currency_code")
    private String unOfficialCurrency;


    public Balance() {

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

    public boolean hasAvailableBalanceForPayment(Double paymentAmount) {
        Assert.notNull(paymentAmount, "paymentAmount cannot be null");
        Assert.isTrue(paymentAmount.doubleValue() > 0, "paymentAmount cannot be 0");
        return available.doubleValue() > paymentAmount.doubleValue();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
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

        private Balance balance = new Balance();

        public Builder available(Double available) {
            balance.available = available;
            return this;
        }

        public Builder current(Double current) {
            balance.current = current;
            return this;
        }

        public Builder limit(Double limit) {
            balance.limit = limit;
            return this;
        }

        public Builder currencyCode(String currencyCode) {
            balance.currencyCode = currencyCode;
            return this;
        }

        public Builder unOfficialCurrency(String unOfficialCurrency) {
            balance.unOfficialCurrency = unOfficialCurrency;
            return this;
        }

        public Balance build() {
            return balance;
        }
    }
}

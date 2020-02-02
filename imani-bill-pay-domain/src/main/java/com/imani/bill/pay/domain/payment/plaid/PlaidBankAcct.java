package com.imani.bill.pay.domain.payment.plaid;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.imani.bill.pay.domain.payment.Balance;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

/**
 * Represents an actual Plaid Bank Account.
 *
 * @author manyce400
 */
@Embeddable
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaidBankAcct {



    @JsonProperty("account_id")
    @Column(name="PlaidBankAcctID", nullable=true, length=200)
    private String accountID;

    @Column(name="PlaidBankAcctName", nullable=true, length=200)
    private String name;

    @JsonProperty("official_name")
    @Column(name="PlaidBankAcctOfficialName", nullable=true, length=200)
    private String officialName;

    @JsonProperty("subtype")
    @Column(name="PlaidBankAcctSubType", nullable=true, length=100)
    private String subType;

    @Column(name="PlaidBankAcctType", nullable=true, length=100)
    private String type;

    // Plaid API Access token required to access details for account
    @Column(name="PlaidAccessToken", nullable=false, length=300)
    public String plaidAccessToken;

    @Transient
    private Balance balances;


    public PlaidBankAcct() {

    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOfficialName() {
        return officialName;
    }

    public void setOfficialName(String officialName) {
        this.officialName = officialName;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPlaidAccessToken() {
        return plaidAccessToken;
    }

    public void setPlaidAccessToken(String plaidAccessToken) {
        this.plaidAccessToken = plaidAccessToken;
    }

    public Balance getBalances() {
        return balances;
    }

    public void setBalances(Balance balances) {
        this.balances = balances;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("accountID", accountID)
                .append("name", name)
                .append("officialName", officialName)
                .append("subType", subType)
                .append("type", type)
                .append("plaidAccessToken", plaidAccessToken)
                .append("balances", balances)
                .toString();
    }

    public static class Builder {

        private PlaidBankAcct plaidBankAcct = new PlaidBankAcct();

        public Builder accountID(String accountID) {
            plaidBankAcct.accountID = accountID;
            return this;
        }

        public Builder name(String name) {
            plaidBankAcct.name = name;
            return this;
        }

        public Builder officialName(String officialName) {
            plaidBankAcct.officialName = officialName;
            return this;
        }

        public Builder type(String type) {
            plaidBankAcct.type = type;
            return this;
        }

        public Builder subType(String subType) {
            plaidBankAcct.subType = subType;
            return this;
        }

        public Builder plaidAccessToken(String plaidAccessToken) {
            plaidBankAcct.plaidAccessToken = plaidAccessToken;
            return this;
        }

        public Builder balances(Balance balances) {
            plaidBankAcct.balances =  balances;
            return this;
        }

        public PlaidBankAcct build() {
            return plaidBankAcct;
        }

    }
}

package com.imani.bill.pay.domain.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Represents an actual Plaid Bank Account.
 *
 * @author manyce400
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaidBankAccount {


    @JsonProperty("account_id")
    private String accountID;

    private String name;

    @JsonProperty("official_name")
    private String officialName;

    private String subType;

    private String type;

    private Balance balances;


    public PlaidBankAccount() {

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
                .append("type", type)
                .append("subType", subType)
                .append("balances", balances)
                .toString();
    }


    public static class Builder {

        private PlaidBankAccount plaidBankAccount = new PlaidBankAccount();

        public Builder accountID(String accountID) {
            plaidBankAccount.accountID = accountID;
            return this;
        }

        public Builder name(String name) {
            plaidBankAccount.name = name;
            return this;
        }

        public Builder officialName(String officialName) {
            plaidBankAccount.officialName = officialName;
            return this;
        }

        public Builder type(String type) {
            plaidBankAccount.type = type;
            return this;
        }

        public Builder subType(String subType) {
            plaidBankAccount.subType = subType;
            return this;
        }

        public Builder balances(Balance balances) {
            plaidBankAccount.balances =  balances;
            return this;
        }

        public PlaidBankAccount build() {
            return plaidBankAccount;
        }

    }
}

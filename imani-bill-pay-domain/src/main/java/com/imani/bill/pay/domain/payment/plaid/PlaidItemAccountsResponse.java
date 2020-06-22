package com.imani.bill.pay.domain.payment.plaid;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author manyce400
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaidItemAccountsResponse extends PlaidAPIResponse {


    @JsonProperty("item")
    private PlaidItemInfo plaidItemInfo;

    private List<PlaidBankAcct> accounts = new ArrayList<>();


    public PlaidItemAccountsResponse() {

    }

    public PlaidItemInfo getPlaidItemInfo() {
        return plaidItemInfo;
    }

    public void setPlaidItemInfo(PlaidItemInfo plaidItemInfo) {
        this.plaidItemInfo = plaidItemInfo;
    }

    public List<PlaidBankAcct> getAccounts() {
        return ImmutableList.copyOf(accounts);
    }

    public void setAccounts(List<PlaidBankAcct> accounts) {
        this.accounts = accounts;
    }

    public PlaidBankAcct getFirst() {
        return accounts.get(0);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("plaidItemInfo", plaidItemInfo)
                .append("accounts", accounts)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final PlaidItemAccountsResponse plaidItemAccountsResponse = new PlaidItemAccountsResponse();

        public Builder plaidItemInfo(PlaidItemInfo plaidItemInfo) {
            plaidItemAccountsResponse.plaidItemInfo = plaidItemInfo;
            return this;
        }

        public Builder plaidBankAcct(PlaidBankAcct plaidBankAcct) {
            plaidItemAccountsResponse.accounts.add(plaidBankAcct);
            return this;
        }

        public PlaidItemAccountsResponse build() {
            return plaidItemAccountsResponse;
        }
    }
}

package com.imani.bill.pay.domain.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author manyce400
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankAccountList {

    private List<PlaidBankAccount> accounts = new ArrayList<>();

    public BankAccountList() {

    }

    public List<PlaidBankAccount> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<PlaidBankAccount> accounts) {
        this.accounts = accounts;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("accounts", accounts)
                .toString();
    }
}

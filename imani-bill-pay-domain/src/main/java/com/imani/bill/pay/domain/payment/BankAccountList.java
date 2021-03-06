package com.imani.bill.pay.domain.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.payment.plaid.PlaidBankAcct;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author manyce400
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankAccountList {

    private List<PlaidBankAcct> accounts = new ArrayList<>();

    public BankAccountList() {

    }

    public List<PlaidBankAcct> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<PlaidBankAcct> accounts) {
        this.accounts = accounts;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("accounts", accounts)
                .toString();
    }
}

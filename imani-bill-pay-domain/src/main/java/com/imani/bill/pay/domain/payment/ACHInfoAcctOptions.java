package com.imani.bill.pay.domain.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author manyce400
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
class ACHInfoAcctOptions {


    @JsonProperty("account_ids")
    private List<String> accountIDs = new ArrayList<>();

    public ACHInfoAcctOptions() {

    }

    public String[] getAccountIDs() {
        return  accountIDs.toArray(new String[0]);
    }


    public void addAccountID(String accountID) {
        Assert.notNull(accountID, "accountID cannot be null");
        accountIDs.add(accountID);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("accountIDs", accountIDs)
                .toString();
    }

}

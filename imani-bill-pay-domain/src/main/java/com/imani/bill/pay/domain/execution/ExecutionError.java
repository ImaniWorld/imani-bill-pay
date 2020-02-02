package com.imani.bill.pay.domain.execution;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

/**
 * Execution errors that occur as part of attempting to execute any request or process as part of Imani BillPay operations
 *
 * @author manyce400
 */
public class ExecutionError {


    private final String error;

    public ExecutionError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("error", error)
                .toString();
    }


    public static ExecutionError of(String error) {
        Assert.notNull(error, "Error cannot be null");
        return new ExecutionError(error);
    }
}

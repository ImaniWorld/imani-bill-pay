package com.imani.bill.pay.service.validation;

import com.imani.bill.pay.domain.execution.ExecutionResult;

/**
 * @author manyce400
 */
public interface IExecutionAdvice<I> {

    public void addExecutionAdvice(ExecutionResult executionResult, I input);

}

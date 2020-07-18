package com.imani.bill.pay.domain.execution;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Domain object encapsulating the results of executing any requests, or processes as part of Imani BillPay platform.
 *
 * @author manyce400
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExecutionResult<O> {

    private O result;

    private Set<ValidationAdvice> validationAdvices = new HashSet<>();

    private Set<ExecutionError> executionErrors = new HashSet<>();

    public ExecutionResult() {

    }

    public Optional<O> getResult() {
        return result == null ? Optional.empty() : Optional.of(result);
    }

    public void setResult(O result) {
        this.result = result;
    }

    public boolean isExecutionSuccessful() {
        return validationAdvices.isEmpty() && executionErrors.isEmpty();
    }

    public boolean hasValidationAdvice() {
        return !validationAdvices.isEmpty();
    }

    public boolean hasExecutionError() {
        return !executionErrors.isEmpty();
    }

    public void addValidationAdvice(ValidationAdvice validationAdvice) {
        Assert.notNull(validationAdvice, "ValidationAdvice cannot be null");
        validationAdvices.add(validationAdvice);
    }

    public void addValidationAdvices(Set<ValidationAdvice> validationAdvices) {
        Assert.notNull(validationAdvices, "validationAdvices cannot be null");
        validationAdvices.forEach(validationAdvice -> {
            addValidationAdvice(validationAdvice);
        });
    }

    public void addExecutionError(ExecutionError executionError) {
        Assert.notNull(executionError, "ExecutionError cannot be null");
        executionErrors.add(executionError);
    }

    public void addExecutionErrors(Set<ExecutionError> executionErrors) {
        Assert.notNull(executionErrors, "executionErrors cannot be null");
        executionErrors.forEach(executionError -> {
            addExecutionError(executionError);
        });
    }

    public Set<ValidationAdvice> getValidationAdvices() {
        return ImmutableSet.copyOf(validationAdvices);
    }

    public Set<ExecutionError> getExecutionErrors() {
        return ImmutableSet.copyOf(executionErrors);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("result", result)
                .append("validationAdvices", validationAdvices)
                .append("executionErrors", executionErrors)
                .toString();
    }

    public static <O> ExecutionResult<O> withValidationAdvice(String ...advices){
        ExecutionResult executionResult = new ExecutionResult();

        for(String advice : advices) {
            executionResult.addValidationAdvice(ValidationAdvice.newInstance(advice));
        }

        return executionResult;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private ExecutionResult executionResult = new ExecutionResult();

        public <O> Builder result(O result) {
            executionResult.result = result;
            return this;
        }

        public Builder addValidationAdvice(ValidationAdvice validationAdvice) {
            executionResult.addValidationAdvice(validationAdvice);
            return this;
        }

        public Builder addValidationAdvice(String advice) {
            Assert.notNull(advice, "advice cannot be null");
            executionResult.addValidationAdvice(ValidationAdvice.newInstance(advice));
            return this;
        }

        public ExecutionResult build() {
            return executionResult;
        }

    }

}

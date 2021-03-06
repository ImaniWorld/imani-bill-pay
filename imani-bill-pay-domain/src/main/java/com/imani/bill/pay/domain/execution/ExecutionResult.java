package com.imani.bill.pay.domain.execution;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.ImmutableSet;
import com.imani.bill.pay.domain.platform.PlatformActionRequiredE;
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

    private Object output;

    private Set<ValidationAdvice> validationAdvices = new HashSet<>();

    private Set<ExecutionError> executionErrors = new HashSet<>();

    private Set<PlatformActionRequiredE> platformActionRequiredSet = new HashSet<>();

    public ExecutionResult() {

    }

    public ExecutionResult(O result) {
        this.result = result;
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

    public void addPlatformActionRequiredE(PlatformActionRequiredE platformActionRequiredE) {
        Assert.notNull(platformActionRequiredE, "PlatformActionRequiredE cannot be null");
        platformActionRequiredSet.add(platformActionRequiredE);
    }

    public void addPlatformActionRequiredESet(Set<PlatformActionRequiredE> input) {
        Assert.notNull(platformActionRequiredSet, "platformActionRequiredSet cannot be null");
        input.forEach(platformActionRequiredE -> {
            addPlatformActionRequiredE(platformActionRequiredE);
        });
    }

    public Set<PlatformActionRequiredE> getPlatformActionRequiredSet() {
        return ImmutableSet.copyOf(platformActionRequiredSet);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("result", result)
                .append("validationAdvices", validationAdvices)
                .append("executionErrors", executionErrors)
                .append("platformActionRequiredSet", platformActionRequiredSet)
                .toString();
    }

    public static <O> ExecutionResult<O> withValidationAdvice(String ...advices){
        ExecutionResult executionResult = new ExecutionResult();

        for(String advice : advices) {
            executionResult.addValidationAdvice(ValidationAdvice.newInstance(advice));
        }

        return executionResult;
    }

}
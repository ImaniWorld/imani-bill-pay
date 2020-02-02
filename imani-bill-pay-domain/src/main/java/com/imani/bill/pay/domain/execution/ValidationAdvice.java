package com.imani.bill.pay.domain.execution;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author manyce400
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidationAdvice {


    private final String advice;

    public ValidationAdvice(String advice) {
        this.advice = advice;
    }


    public String getAdvice() {
        return advice;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("advice", advice)
                .toString();
    }


    public static ValidationAdvice newInstance(String advice) {
        return new ValidationAdvice(advice);
    }


}

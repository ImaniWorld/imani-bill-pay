package com.imani.bill.pay.service.validation;

import com.imani.bill.pay.domain.execution.ValidationAdvice;

import java.util.Set;

/**
 * @author manyce400
 * @param <O> input object required to perform validation
 */
public interface IValidationAdviceService<O> {


    // Execute validation for given validation object
    public Set<ValidationAdvice> getAdvice(O validationObject);

}

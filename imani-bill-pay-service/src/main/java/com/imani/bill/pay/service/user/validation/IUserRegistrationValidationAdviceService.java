package com.imani.bill.pay.service.user.validation;

import com.imani.bill.pay.domain.execution.ValidationAdvice;
import com.imani.bill.pay.domain.user.UserRecord;

import java.util.Set;

/**
 * @author manyce400
 */
public interface IUserRegistrationValidationAdviceService {

    // For given user record validate to make sure that user can be registered successfully
    public Set<ValidationAdvice> getAdvice(UserRecord userRecord);

}

package com.imani.bill.pay.service.user.validation;

import com.imani.bill.pay.domain.execution.ValidationAdvice;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.repository.IUserRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Set;

/**
 * This implementation verifies that the registration details of a provided UserRecord are in order and that the user can
 * access Imani BillPay platform based on the verification.
 *
 * @author manyce400
 */
@Service(RegistrationValidationAdviceService.SPRING_BEAN)
public class RegistrationValidationAdviceService implements IPlatformAccessValidationAdviceService {



    @Autowired
    private IUserRecordRepository iUserRecordRepository;


    public static final String SPRING_BEAN = "com.imani.bill.pay.service.user.validation.RegistrationValidationAdviceService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(RegistrationValidationAdviceService.class);



    @Override
    public Set<ValidationAdvice> getAdvice(UserRecord userRecord) {
        Assert.notNull(userRecord, "UserRecord validation object cannot be null");

        LOGGER.info("Executing UserRecord registration details validation for user:=> {}", userRecord.getEmbeddedContactInfo().getEmail());
        Set<ValidationAdvice> validationAdvices = new HashSet<>();

        // Refresh user details before performing validation
        userRecord = iUserRecordRepository.findByUserEmail(userRecord.getEmbeddedContactInfo().getEmail());
        addAccountLockedAdvice(userRecord, validationAdvices);
        return validationAdvices;
    }


    void addAccountLockedAdvice(UserRecord userRecord, Set<ValidationAdvice> validationAdvices) {
        if(userRecord.isAccountLocked()) {
            LOGGER.info("Detected that UserRecord account is locked, user registration is currently not valid");
            validationAdvices.add(ValidationAdvice.newInstance("User account Imani BillPay access is currently locked"));
        }
    }

}

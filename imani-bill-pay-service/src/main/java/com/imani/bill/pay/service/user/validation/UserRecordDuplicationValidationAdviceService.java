package com.imani.bill.pay.service.user.validation;

import com.imani.bill.pay.domain.execution.ValidationAdvice;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.repository.IUserRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Set;

/**
 * @author manyce400
 */
@Order(2)
@Service(UserRecordDuplicationValidationAdviceService.SPRING_BEAN)
public class UserRecordDuplicationValidationAdviceService implements IUserRegistrationValidationAdviceService  {


    @Autowired
    private IUserRecordRepository iUserRecordRepository;


    public static final String SPRING_BEAN = "com.imani.bill.pay.service.user.validation.UserRecordDuplicationValidationAdviceService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(UserRecordDuplicationValidationAdviceService.class);

    @Override
    public Set<ValidationAdvice> getAdvice(UserRecord userRecord) {
        Assert.notNull(userRecord, "UserRecord cannot be null");

        LOGGER.info("Executing user record duplication check for new userRecord:=> {}", userRecord);

        Set<ValidationAdvice> validationAdvices = new HashSet<>();

        if (userRecord.getEmbeddedContactInfo() != null) {
            // We cant allow a new user to register with an existing email and phone number
            UserRecord jpaUserRecord = iUserRecordRepository.findByUserEmail(userRecord.getEmbeddedContactInfo().getEmail());
            if(jpaUserRecord != null) {
                validationAdvices.add(ValidationAdvice.newInstance("An existing user found with the same email provided."));
            }

            jpaUserRecord = iUserRecordRepository.findByMobilePhone(userRecord.getEmbeddedContactInfo().getMobilePhone());
            if(jpaUserRecord != null) {
                validationAdvices.add(ValidationAdvice.newInstance("An existing user found with the same mobile phone number provided."));
            }
        }

        return validationAdvices;
    }

}

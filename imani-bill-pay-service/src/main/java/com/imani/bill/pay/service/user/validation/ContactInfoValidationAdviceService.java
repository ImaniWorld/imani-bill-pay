package com.imani.bill.pay.service.user.validation;

import com.imani.bill.pay.domain.contact.EmbeddedContactInfo;
import com.imani.bill.pay.domain.execution.ValidationAdvice;
import com.imani.bill.pay.domain.user.UserRecord;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author manyce400
 */
@Order(1)
@Service(ContactInfoValidationAdviceService.SPRING_BEAN)
public class ContactInfoValidationAdviceService implements IUserRegistrationValidationAdviceService {




    public static final String SPRING_BEAN = "com.imani.bill.pay.service.user.validation.ContactInfoValidationAdviceService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ContactInfoValidationAdviceService.class);


    @Override
    public Set<ValidationAdvice> getAdvice(UserRecord userRecord) {
        Assert.notNull(userRecord, "UserRecord cannot be null");

        LOGGER.info("Executing contact information validation on registration for userRecord:=> {}", userRecord);

        Set<ValidationAdvice> validationAdvices = new HashSet<>();

        if(userRecord.getEmbeddedContactInfo() == null) {
            validationAdvices.add(ValidationAdvice.newInstance("Contact information for user is missing"));
        } else {
            addEmailAdvice(userRecord.getEmbeddedContactInfo(), validationAdvices);
            addMobilePhoneAdvice(userRecord.getEmbeddedContactInfo(), validationAdvices);
            addPreferredContactTypeAdvice(userRecord.getEmbeddedContactInfo(), validationAdvices);
        }


        return validationAdvices;
    }


    void addEmailAdvice(EmbeddedContactInfo embeddedContactInfo, Set<ValidationAdvice> validationAdvices) {
        if(StringUtils.isEmpty(embeddedContactInfo.getEmail())) {
            validationAdvices.add(ValidationAdvice.newInstance("Email address information is missing"));
        }
    }

    void addMobilePhoneAdvice(EmbeddedContactInfo embeddedContactInfo, Set<ValidationAdvice> validationAdvices) {
        // TODO add phone format validation to make sure it matches US/different country formats later
        if(StringUtils.isEmpty(embeddedContactInfo.getMobilePhone())) {
            validationAdvices.add(ValidationAdvice.newInstance("Mobile phone information is missing"));
        }
    }

    void addPreferredContactTypeAdvice(EmbeddedContactInfo embeddedContactInfo, Set<ValidationAdvice> validationAdvices) {
        // TODO add phone format validation to make sure it matches US/different country formats later
        if(embeddedContactInfo.getPreferredContactType() == null) {
            validationAdvices.add(ValidationAdvice.newInstance("Preffered contact type information is missing"));
        }
    }

}

package com.imani.bill.pay.service.user;

import com.imani.bill.pay.domain.gateway.APIGatewayEventStatusE;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.UserRecordEvent;
import com.imani.bill.pay.domain.user.repository.IUserRecordRepository;
import com.imani.bill.pay.service.encryption.ClearTextEncryptionService;
import com.imani.bill.pay.service.encryption.IClearTextEncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

/**
 * @author manyce400
 */
@Service(UserRecordManagerService.SPRING_BEAN)
public class UserRecordManagerService implements IUserRecordManagerService {



    @Autowired
    private IUserRecordRepository iUserRecordRepository;


    @Autowired
    @Qualifier(ClearTextEncryptionService.SPRING_BEAN)
    private IClearTextEncryptionService clearTextEncryptionService;


    public static final String SPRING_BEAN = "com.imani.bill.pay.service.user.UserRecordManagerService";


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(UserRecordManagerService.class);


    @Override
    public UserRecordEvent getUserRecord(UserRecord userRecord) {
        Assert.notNull(userRecord, "UserRecord cannot be null");
        Assert.notNull(userRecord.getEmbeddedContactInfo(), "EmbeddedContactInfo cannot be null");


        // Execute validations, find matching user
        UserRecord jpaUserRecord = iUserRecordRepository.findByUserEmailAndMobilePhone(userRecord.getEmbeddedContactInfo().getEmail(), userRecord.getEmbeddedContactInfo().getMobilePhone());

        if (jpaUserRecord != null) {
            LOGGER.info("Found UserRecord details for user with email:=> {}", userRecord.getEmbeddedContactInfo().getEmail());
            UserRecordEvent userRecordEvent = getUserRecordEventOnSucess(jpaUserRecord);
            return userRecordEvent;
        }

        LOGGER.info("No existing user found with email:=> {} cannot return UserRecord", userRecord.getEmbeddedContactInfo().getEmail());
        UserRecordEvent userRecordEvent = getUserRecordEventOnInvalidUser(userRecord);
        return userRecordEvent;
    }

    @Transactional
    @Override
    public UserRecordEvent registerUserRecord(UserRecord userRecord) {
        Assert.notNull(userRecord, "UserRecord cannot be null");
        Assert.notNull(userRecord.getEmbeddedContactInfo(), "EmbeddedContactInfo cannot be null");

        // Execute validations, make sure no existing user with the same email and mobile phone number
        UserRecord jpaUserRecord = iUserRecordRepository.findByUserEmailAndMobilePhone(userRecord.getEmbeddedContactInfo().getEmail(), userRecord.getEmbeddedContactInfo().getMobilePhone());

        if (jpaUserRecord == null) {
            LOGGER.info("Registering new UserRecord for user with email:=> {}", userRecord.getEmbeddedContactInfo().getEmail());
            String encoded = clearTextEncryptionService.encryptClearText(userRecord.getPassword());
            userRecord.setPassword(encoded);
            iUserRecordRepository.save(userRecord);
            UserRecordEvent userRecordEvent = getUserRecordEventOnSucess(userRecord);
            return userRecordEvent;
        }

        LOGGER.info("Existing user found with same credentials. Cannot register new user with email:=> {} and mobilePhone: {}", userRecord.getEmbeddedContactInfo().getEmail(), userRecord.getEmbeddedContactInfo().getMobilePhone());
        UserRecordEvent userRecordEvent = getUserRecordEventOnInvalidUser(userRecord);
        return userRecordEvent;
    }


    @Transactional
    @Override
    public UserRecordEvent updateUserRecord(UserRecord userRecord) {
        Assert.notNull(userRecord, "UserRecord cannot be null");
        Assert.notNull(userRecord.getEmbeddedContactInfo(), "EmbeddedContactInfo cannot be null");


        // Execute validations, find matching user
        UserRecord jpaUserRecord = iUserRecordRepository.findByUserEmailAndMobilePhone(userRecord.getEmbeddedContactInfo().getEmail(), userRecord.getEmbeddedContactInfo().getMobilePhone());

        if (jpaUserRecord != null) {
            // This API will not allow user to override their Email, Mobile Number or Password
            jpaUserRecord.updateSafeFieldsWherePresent(userRecord);
            iUserRecordRepository.save(userRecord);
            UserRecordEvent userRecordEvent = getUserRecordEventOnSucess(userRecord);
            return userRecordEvent;
        }

        LOGGER.info("No existing user found with email:=> {} cannot update UserRecord....", userRecord.getEmbeddedContactInfo().getEmail());
        UserRecordEvent userRecordEvent = getUserRecordEventOnInvalidUser(userRecord);
        return userRecordEvent;
    }


    UserRecordEvent getUserRecordEventOnSucess(UserRecord userRecord) {
        UserRecordEvent userRecordEvent = UserRecordEvent.builder()
                .userRecord(userRecord)
                .apiGatewayEventStatusE(APIGatewayEventStatusE.Success)
                .build();
        return userRecordEvent;
    }


    UserRecordEvent getUserRecordEventOnInvalidUser(UserRecord userRecord) {
        UserRecordEvent userRecordEvent = UserRecordEvent.builder()
                .userRecord(userRecord)
                .apiGatewayEventStatusE(APIGatewayEventStatusE.InvalidRequest)
                .build();
        return userRecordEvent;
    }

}

package com.imani.bill.pay.service.user;

import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.execution.ValidationAdvice;
import com.imani.bill.pay.domain.gateway.APIGatewayEvent;
import com.imani.bill.pay.domain.gateway.GenericAPIGatewayResponse;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.gateway.UserRecordRequest;
import com.imani.bill.pay.domain.user.repository.IUserRecordRepository;
import com.imani.bill.pay.service.encryption.ClearTextEncryptionService;
import com.imani.bill.pay.service.encryption.IClearTextEncryptionService;
import com.imani.bill.pay.service.user.validation.IUserRegistrationValidationAdviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

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


    //  Inject all User registration advice services
    @Autowired
    private List<IUserRegistrationValidationAdviceService> iUserRegistrationValidationAdviceServiceList;


    public static final String SPRING_BEAN = "com.imani.bill.pay.service.user.UserRecordManagerService";


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(UserRecordManagerService.class);


    @Override
    public APIGatewayEvent<UserRecordRequest, GenericAPIGatewayResponse> fetchUserRecord(UserRecordRequest userRecordRequest) {
        Assert.notNull(userRecordRequest, "UserRecordRequest cannot be null");
        Assert.notNull(userRecordRequest.getExecUserRecord().get().getEmbeddedContactInfo(), "EmbeddedContactInfo cannot be null");

        UserRecord userRecord = userRecordRequest.getExecUserRecord().get();
        LOGGER.info("Fetching update UserRecord for => {}", userRecord.getEmbeddedContactInfo().getEmail());

        // Execute validations, find matching user
        UserRecord jpaUserRecord = iUserRecordRepository.findByUserEmail(userRecord.getEmbeddedContactInfo().getEmail());

        if (jpaUserRecord != null) {
            LOGGER.info("Found UserRecord details for user with email:=> {}", userRecord.getEmbeddedContactInfo().getEmail());
            return getUserRecordEventOnSucess(jpaUserRecord);
        }

        LOGGER.info("No existing user found with email:=> {} cannot return UserRecord", userRecord.getEmbeddedContactInfo().getEmail());
        return getUserRecordEventOnInvalidUser(userRecord);
    }

    @Transactional
    @Override
    public APIGatewayEvent<UserRecordRequest, GenericAPIGatewayResponse> registerUserRecord(UserRecordRequest userRecordRequest) {
        Assert.notNull(userRecordRequest, "UserRecordRequest cannot be null");
        Assert.notNull(userRecordRequest.getExecUserRecord().get().getEmbeddedContactInfo(), "EmbeddedContactInfo cannot be null");

        // Execute validations, make sure no existing user with the same email and mobile phone number
        UserRecord userRecord = userRecordRequest.getExecUserRecord().get();
        UserRecord jpaUserRecord = iUserRecordRepository.findByUserEmailAndMobilePhone(userRecord.getEmbeddedContactInfo().getEmail(), userRecord.getEmbeddedContactInfo().getMobilePhone());

        if (jpaUserRecord == null) {
            LOGGER.info("Registering new UserRecord for user with email:=> {}", userRecord.getEmbeddedContactInfo().getEmail());
            String encoded = clearTextEncryptionService.encryptClearText(userRecord.getPassword());
            userRecord.setPassword(encoded);
            iUserRecordRepository.save(userRecord);
            return getUserRecordEventOnSucess(userRecord);
        }

        LOGGER.info("Existing user found with same credentials. Cannot register new user with email:=> {} and mobilePhone: {}", userRecord.getEmbeddedContactInfo().getEmail(), userRecord.getEmbeddedContactInfo().getMobilePhone());
        return getUserRecordEventOnInvalidUser(userRecord);
    }

    @Override
    public ExecutionResult registerUserRecord(UserRecord userRecord) {
        Assert.notNull(userRecord, "UserRecord cannot be null");

        LOGGER.info("Attempting to register userRecord:=> {}", userRecord);

        // Create ExecutionResult and begin validating userRecord
        ExecutionResult executionResult = new ExecutionResult();

        iUserRegistrationValidationAdviceServiceList.forEach(adviceService -> {
            Set<ValidationAdvice> validationAdvices = adviceService.getAdvice(userRecord);
            executionResult.addValidationAdvices(validationAdvices);
        });

        if(!executionResult.hasValidationAdvice()) {
            // No validation advice, we are good to register this user
            LOGGER.info("Registering new UserRecord for user with email:=> {}", userRecord.getEmbeddedContactInfo().getEmail());
            String encoded = clearTextEncryptionService.encryptClearText(userRecord.getPassword());
            userRecord.setPassword(encoded);
            iUserRecordRepository.save(userRecord);
            return executionResult;
        } else {
            LOGGER.warn("Validation advices found, skipping registration of user");
            return executionResult;
        }


    }

    @Transactional
    @Override
    public APIGatewayEvent<UserRecordRequest, GenericAPIGatewayResponse> updateUserRecord(UserRecordRequest userRecordRequest) {
        Assert.notNull(userRecordRequest, "UserRecordRequest cannot be null");
        Assert.notNull(userRecordRequest.getExecUserRecord().get().getEmbeddedContactInfo(), "EmbeddedContactInfo cannot be null");


        // Execute validations, find matching user
        UserRecord userRecord = userRecordRequest.getExecUserRecord().get();
        UserRecord jpaUserRecord = iUserRecordRepository.findByUserEmailAndMobilePhone(userRecord.getEmbeddedContactInfo().getEmail(), userRecord.getEmbeddedContactInfo().getMobilePhone());

        if (jpaUserRecord != null) {
            // This API will not allow user to override their Email, Mobile Number or Password
            jpaUserRecord.updateSafeFieldsWherePresent(userRecord);
            iUserRecordRepository.save(userRecord);
            return getUserRecordEventOnSucess(userRecord);
        }

        LOGGER.info("No existing user found with email:=> {} cannot update UserRecord....", userRecord.getEmbeddedContactInfo().getEmail());
        return getUserRecordEventOnInvalidUser(userRecord);
    }


    APIGatewayEvent<UserRecordRequest, GenericAPIGatewayResponse> getUserRecordEventOnSucess(UserRecord userRecord) {
        GenericAPIGatewayResponse genericAPIGatewayResponse = GenericAPIGatewayResponse.getSuccessGenericAPIGatewayResponse();
        return APIGatewayEvent.builder()
                .responseBody(genericAPIGatewayResponse)
                .eventUserRecord(userRecord)
                .build();
    }


    APIGatewayEvent<UserRecordRequest, GenericAPIGatewayResponse> getUserRecordEventOnInvalidUser(UserRecord userRecord) {
        GenericAPIGatewayResponse genericAPIGatewayResponse = GenericAPIGatewayResponse.getInvalidRequestGenericAPIGatewayResponse("Invalid user credentials supplied.");
        return APIGatewayEvent.builder()
                .responseBody(genericAPIGatewayResponse)
                .eventUserRecord(userRecord)
                .build();

    }

}

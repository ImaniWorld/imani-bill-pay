package com.imani.bill.pay.service.user;


import com.imani.bill.pay.domain.gateway.APIGatewayEvent;
import com.imani.bill.pay.domain.gateway.GenericAPIGatewayResponse;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.gateway.UserRecordRequest;
import com.imani.bill.pay.domain.user.repository.IUserRecordRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

/**
 * @author manyce400
 */
@Service(UserRecordAuthService.SPRING_BEAN)
public class UserRecordAuthService implements IUserRecordAuthService {




    @Autowired
    private IUserRecordRepository iUserRecordRepository;


    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    @Qualifier(UserLoginStatisticService.SPRING_BEAN)
    private IUserLoginStatisticService iUserLoginStatisticService;


    public static final String SPRING_BEAN = "com.imani.bill.pay.service.user.UserRecordAuthService";


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(UserRecordAuthService.class);



    @Transactional
    @Override
    public APIGatewayEvent<UserRecordRequest, GenericAPIGatewayResponse> authenticateAndLogInUserRecord(UserRecordRequest userRecordRequest) {
        Assert.notNull(userRecordRequest, "UserRecordRequest cannot be null");
        Assert.isTrue(userRecordRequest.getExecUserRecord().isPresent(), "UserRecord to login cannot be empty");
        Assert.isTrue(userRecordRequest.getUserLoginStatistic().isPresent(), "UserLoginStatistic cannot be empty");

        UserRecord userRecord = userRecordRequest.getExecUserRecord().get();
        String email = userRecord.getEmbeddedContactInfo().getEmail();
        String password = userRecord.getPassword();

        LOGGER.info("Attempting to login and authenticate userRecord:=>  {}", email);

        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            UserRecord jpaUserRecord = iUserRecordRepository.findByUserEmail(email);
            recordSuccessfulLogin(jpaUserRecord);
            iUserLoginStatisticService.recordUserLoginStatistic(jpaUserRecord, userRecordRequest.getUserLoginStatistic().get());
            return getSuccesfullAPIGatewayEvent(jpaUserRecord);
        } catch (BadCredentialsException e) {
            LOGGER.info("Invalid credentials supplied for UserRecord:=> {} abandoning login process...", userRecord.getEmbeddedContactInfo().getEmail());
            Integer unsucessfulLoginAttempts = trackUnsuccessfulLoginAttempts(userRecord);
            return getBadCredentialsUserRecordAuthentication(userRecord, unsucessfulLoginAttempts);
        } catch (LockedException e) {
            LOGGER.info("Account is currently locked for UserRecord:=> {} abandoning login process...", userRecord.getEmbeddedContactInfo().getEmail());
            Integer unsucessfulLoginAttempts = trackUnsuccessfulLoginAttempts(userRecord);
            return getLockedUserRecordAuthentication(userRecord, unsucessfulLoginAttempts);
        }
    }


    @Transactional
    @Override
    public APIGatewayEvent<UserRecordRequest, GenericAPIGatewayResponse> authenticateAndLogOutUserRecord(UserRecordRequest userRecordRequest) {
        Assert.notNull(userRecordRequest, "UserRecordRequest cannot be null");
        Assert.isTrue(userRecordRequest.getExecUserRecord().isPresent(), "UserRecord to login cannot be empty");
        Assert.isTrue(userRecordRequest.getUserLoginStatistic().isPresent(), "UserLoginStatistic cannot be empty");

        UserRecord userRecord = userRecordRequest.getExecUserRecord().get();

        // Verify user by email and that the user is also currently logged in
        UserRecord jpaUserRecord = iUserRecordRepository.findByUserEmail(userRecord.getEmbeddedContactInfo().getEmail());

        if(jpaUserRecord.isLoggedIn()) {
            LOGGER.info("Logging out UserRecord with email:=> {}", userRecord.getEmbeddedContactInfo().getEmail());
            jpaUserRecord.setLastLogoutDate(DateTime.now());
            jpaUserRecord.setLoggedIn(false);
            iUserRecordRepository.save(jpaUserRecord);

            // Update Login statistic
            iUserLoginStatisticService.recordUserLogoutStatistic(jpaUserRecord, userRecordRequest.getUserLoginStatistic().get());
            return getSuccesfullAPIGatewayEvent(userRecord);
        }

        LOGGER.debug("User is currently not logged in, skipping call to logut...");
        return getUserNotLoggedInRecordAuthentication(jpaUserRecord);
    }

    void recordSuccessfulLogin(UserRecord jpaUserRecord) {
        LOGGER.info("Recording succesful UserRecord login for => {}", jpaUserRecord.getEmbeddedContactInfo().getEmail());
        jpaUserRecord.setLoggedIn(true);
        jpaUserRecord.setUnsuccessfulLoginAttempts(0);
        jpaUserRecord.setLastLoginDate(DateTime.now());
        jpaUserRecord.setLastLogoutDate(null); // reset last logout date
        iUserRecordRepository.save(jpaUserRecord);
    }

    APIGatewayEvent<UserRecordRequest, GenericAPIGatewayResponse> getSuccesfullAPIGatewayEvent(UserRecord userRecord) {
        //UserRecord.getAPISafeVersion(userRecord); // make jpaUserRecord API safe
        GenericAPIGatewayResponse genericAPIGatewayResponse = GenericAPIGatewayResponse.getSuccessGenericAPIGatewayResponse();
        return APIGatewayEvent.builder()
                .responseBody(genericAPIGatewayResponse)
                .eventUserRecord(userRecord)
                .build();
    }

    APIGatewayEvent<UserRecordRequest, GenericAPIGatewayResponse> getBadCredentialsUserRecordAuthentication(UserRecord userRecord, Integer unsuccessfulLoginAttempts) {
        //UserRecord.getAPISafeVersion(userRecord); // make jpaUserRecord API safe
        GenericAPIGatewayResponse genericAPIGatewayResponse = GenericAPIGatewayResponse.getInvalidRequestGenericAPIGatewayResponse("Invalid user credentials supplied.");
        return APIGatewayEvent.builder()
                .responseBody(genericAPIGatewayResponse)
                .eventUserRecord(userRecord)
                .build();
    }

    APIGatewayEvent<UserRecordRequest, GenericAPIGatewayResponse> getLockedUserRecordAuthentication(UserRecord userRecord, Integer unsuccessfulLoginAttempts) {
        //UserRecord.getAPISafeVersion(userRecord); // make jpaUserRecord API safe
        GenericAPIGatewayResponse genericAPIGatewayResponse = GenericAPIGatewayResponse.getInvalidRequestGenericAPIGatewayResponse("UserRecord is currently locked.");
        return APIGatewayEvent.builder()
                .responseBody(genericAPIGatewayResponse)
                .eventUserRecord(userRecord)
                .build();
    }

    APIGatewayEvent<UserRecordRequest, GenericAPIGatewayResponse> getUserNotLoggedInRecordAuthentication(UserRecord userRecord) {
        //UserRecord.getAPISafeVersion(userRecord); // make jpaUserRecord API safe
        GenericAPIGatewayResponse genericAPIGatewayResponse = GenericAPIGatewayResponse.getInvalidRequestGenericAPIGatewayResponse("User is currently not logged in");
        return APIGatewayEvent.builder()
                .responseBody(genericAPIGatewayResponse)
                .eventUserRecord(userRecord)
                .build();
    }

    Integer trackUnsuccessfulLoginAttempts(UserRecord userRecord) {
        // Fetch the actual UserRecord so we can update statistics on number of times this user has tried to login unsuccessfully to lock their account
        UserRecord jpaUserRecord = iUserRecordRepository.findByUserEmail(userRecord.getEmbeddedContactInfo().getEmail());

        if (jpaUserRecord != null) {
            LOGGER.info("Updating failed login statistic for user:=> {}", jpaUserRecord.getEmbeddedContactInfo().getEmail());
            int unsucessfulLoginAttempts = jpaUserRecord.getUnsuccessfulLoginAttempts();

            if(unsucessfulLoginAttempts < 3) {
                LOGGER.info("User has attempted to login unsuccessfully less than 3 times, updating metrics....");
                unsucessfulLoginAttempts++;
                jpaUserRecord.setUnsuccessfulLoginAttempts(unsucessfulLoginAttempts);
                iUserRecordRepository.save(jpaUserRecord);
            } else {
                // User has hit the max number of attempts for login, lockout this user
                LOGGER.info("User has hit the maximum number of login attempts, locking user account....");
                jpaUserRecord.setAccountLocked(true);
                iUserRecordRepository.save(jpaUserRecord);
            }

            return unsucessfulLoginAttempts;
        }

        // No actual user was found so return 0 in this case
        return 0;
    }

}

package com.imani.bill.pay.service.user;


import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.UserRecordAuth;
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
import java.util.List;

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
    public UserRecordAuth authenticateAndLogInUserRecord(UserRecordAuth userRecordAuth) {
        Assert.notNull(userRecordAuth, "UserTransactionGatewayMessage cannot be null");
        Assert.notNull(userRecordAuth.getUserRecord(), "UserRecord cannot be null");
        Assert.notNull(userRecordAuth.getUserRecord().getEmbeddedContactInfo(), "EmbeddedContactInfo cannot be null");
        Assert.isTrue(userRecordAuth.getUserLoginStatistic().isPresent(), "UserLoginStatistic cannot be empty");

        UserRecord userRecord = userRecordAuth.getUserRecord();
        String email = userRecord.getEmbeddedContactInfo().getEmail();
        String password = userRecord.getPassword();

        LOGGER.debug("Attempting to login and authenticate userRecord:=>  {}", email);

        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            UserRecord jpaUserRecord = iUserRecordRepository.findByUserEmail(email);

            // Update Login statistic
            iUserLoginStatisticService.recordUserLoginStatistic(jpaUserRecord, userRecordAuth.getUserLoginStatistic().get());
            return executeUserRecordLogin(userRecord);
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
    public UserRecordAuth authenticateAndLogOutUserRecord(UserRecordAuth userRecordAuth) {
        Assert.notNull(userRecordAuth, "UserTransactionGatewayMessage cannot be null");
        Assert.notNull(userRecordAuth.getUserRecord(), "UserRecord cannot be null");
        Assert.notNull(userRecordAuth.getUserRecord().getEmbeddedContactInfo(), "EmbeddedContactInfo cannot be null");
        Assert.isTrue(userRecordAuth.getUserLoginStatistic().isPresent(), "UserLoginStatistic cannot be empty");

        UserRecord userRecord = userRecordAuth.getUserRecord();

        // Verify user by email and that the user is also currently logged in
        UserRecord jpaUserRecord = iUserRecordRepository.findByUserEmail(userRecord.getEmbeddedContactInfo().getEmail());

        if(jpaUserRecord.isLoggedIn()) {
            LOGGER.info("Logging out UserRecord with email:=> {}", userRecord.getEmbeddedContactInfo().getEmail());
            jpaUserRecord.setLastLogoutDate(DateTime.now());
            jpaUserRecord.setLoggedIn(false);
            iUserRecordRepository.save(jpaUserRecord);

            // Update Login statistic
            iUserLoginStatisticService.recordUserLogoutStatistic(jpaUserRecord, userRecordAuth.getUserLoginStatistic().get());
            return getSuccesfulLogOutUserRecordAuthentication(userRecord);
        }

        LOGGER.debug("User is currently not logged in, skipping call to logut...");
        return null;
    }

    UserRecordAuth executeUserRecordLogin(UserRecord userRecord) {
        LOGGER.info("UserRecord has been authenticated successfully completing login steps...");

        // Fetch the actual UserRecord so we can update statistics on number of times this user has tried to login unsuccessfully to lock their account
        UserRecord jpaUserRecord = iUserRecordRepository.findByUserEmail(userRecord.getEmbeddedContactInfo().getEmail());
        jpaUserRecord.setLoggedIn(true);
        jpaUserRecord.setUnsuccessfulLoginAttempts(0);
        jpaUserRecord.setLastLoginDate(DateTime.now());
        jpaUserRecord.setLastLogoutDate(null); // reset last logout date
        iUserRecordRepository.save(jpaUserRecord);
        return getSuccesfulUserRecordAuthentication(userRecord);
    }

    UserRecordAuth getSuccesfulUserRecordAuthentication(UserRecord userRecord) {
        UserRecordAuth userRecordAuth = UserRecordAuth.builder()
                .userRecord(userRecord)
                .build();
        return userRecordAuth;
    }

    UserRecordAuth getSuccesfulLogOutUserRecordAuthentication(UserRecord userRecord) {
        UserRecordAuth userRecordAuth = UserRecordAuth.builder()
                .userRecord(userRecord)
                .build();
        return userRecordAuth;
    }


    UserRecordAuth getBadCredentialsUserRecordAuthentication(UserRecord userRecord, Integer unsucessfulLoginAttempts) {
        UserRecordAuth userRecordAuth = UserRecordAuth.builder()
                .userRecord(userRecord)
                .build();
        return userRecordAuth;
    }

    UserRecordAuth getLockedUserRecordAuthentication(UserRecord userRecord, Integer unsucessfulLoginAttempts) {
        UserRecordAuth userRecordAuth = UserRecordAuth.builder()
                .userRecord(userRecord)
                .build();
        return userRecordAuth;
    }

    Integer trackUnsuccessfulLoginAttempts(UserRecord userRecord) {
        // Fetch the actual UserRecord so we can update statistics on number of times this user has tried to login unsuccessfully to lock their account
        UserRecord jpaUserRecord = iUserRecordRepository.findByUserEmail(userRecord.getEmbeddedContactInfo().getEmail());

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


    @Override
    public List<UserRecord> findAllUserRecord() {
        LOGGER.info("Finding all UserRecord.....");
        return iUserRecordRepository.findAll();
    }


}

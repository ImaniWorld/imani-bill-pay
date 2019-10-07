package com.imani.bill.pay.service.user;

import com.imani.bill.pay.domain.device.DeviceTypeE;
import com.imani.bill.pay.domain.user.UserLoginStatistic;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.repository.IUserLoginStatisticRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * @author manyce400
 */
@Service(UserLoginStatisticService.SPRING_BEAN)
public class UserLoginStatisticService implements IUserLoginStatisticService {



    @Autowired
    private IUserLoginStatisticRepository iUserLoginStatisticRepository;


    public static final String SPRING_BEAN = "com.imani.bill.pay.service.user.UserLoginStatisticService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(UserLoginStatisticService.class);


    @Override
    public boolean validateUserLoginStatistic(UserLoginStatistic userLoginStatistic) {
        Assert.notNull(userLoginStatistic, "userLoginStatistic cannot be null");
        LOGGER.debug("Validating userLoginStatistic:=> {}", userLoginStatistic);

        if(StringUtils.hasLength(userLoginStatistic.getiManiClientVersion())
                && StringUtils.hasLength(userLoginStatistic.getDeviceOS())
                && StringUtils.hasLength(userLoginStatistic.getDeviceVersion())
                && userLoginStatistic.getDeviceTypeE() != null) {
            return true;
        }

        return false;
    }

    @Transactional
    @Override
    public Optional<UserLoginStatistic> recordUserLoginStatistic(UserRecord jpaUserRecord, UserLoginStatistic userLoginStatistic) {
        Assert.notNull(jpaUserRecord, "UserRecord cannot be null");
        Assert.notNull(jpaUserRecord.getId(), "UserRecord is non persistent");
        Assert.notNull(userLoginStatistic, "userLoginStatistic cannot be null");

        LOGGER.debug("Recording Login statistics for user:=> {} from userLoginStatistic:=> {}", jpaUserRecord.getEmbeddedContactInfo().getEmail(), userLoginStatistic);



        if(jpaUserRecord.isLoggedIn()) {
            Optional<UserLoginStatistic> matchingUserLoginStatistic = findMatchingUserLoginStatistic(jpaUserRecord, userLoginStatistic);

            if (!matchingUserLoginStatistic.isPresent()) {
                // Brand new login from a new device configuration so good to save
                LOGGER.debug("User is currently logged in but no existing UserLoginStatistic recording new one....");
                UserLoginStatistic jpaLoginStatistic = UserLoginStatistic.builder()
                        .userRecord(jpaUserRecord)
                        .loginDate(DateTime.now())
                        .deviceTypeE(userLoginStatistic.getDeviceTypeE())
                        .deviceOS(userLoginStatistic.getDeviceOS())
                        .deviceVersion(userLoginStatistic.getDeviceVersion())
                        .iManiClientVersion(userLoginStatistic.getiManiClientVersion())
                        .build();
                iUserLoginStatisticRepository.save(jpaLoginStatistic);
                return Optional.of(jpaLoginStatistic);
            }
        } else {
            // Completely new Login.  User currently doesn't have an existing login so safe to always record.
            LOGGER.debug("User is currently logged in but no existing UserLoginStatistic recording new one....");
            UserLoginStatistic jpaLoginStatistic = UserLoginStatistic.builder()
                    .userRecord(jpaUserRecord)
                    .loginDate(DateTime.now())
                    .deviceTypeE(userLoginStatistic.getDeviceTypeE())
                    .deviceOS(userLoginStatistic.getDeviceOS())
                    .deviceVersion(userLoginStatistic.getDeviceVersion())
                    .iManiClientVersion(userLoginStatistic.getiManiClientVersion())
                    .build();
            iUserLoginStatisticRepository.save(jpaLoginStatistic);
            return Optional.of(jpaLoginStatistic);
        }


        LOGGER.warn("Existing UserLoginStatistic found with login recorded already. Not supposed to happen");
        return Optional.empty();
    }


    @Transactional
    @Override
    public Optional<UserLoginStatistic> recordUserLogoutStatistic(UserRecord jpaUserRecord, UserLoginStatistic userLoginStatistic) {
        Assert.notNull(jpaUserRecord, "UserRecord cannot be null");
        Assert.notNull(jpaUserRecord.getId(), "UserRecord is non persistent");
        Assert.notNull(userLoginStatistic, "userLoginStatistic cannot be null");

        LOGGER.debug("Recording Logout statistics for user:=> {} from userLoginStatistic:=> {}", jpaUserRecord.getEmbeddedContactInfo().getEmail(), userLoginStatistic);

        // Find the UserLoginStatistic that matches what has been passed in, we want to make sure we update the correct record as logged out.
        Optional<UserLoginStatistic> matchingUserLoginStatistic = findMatchingUserLoginStatistic(jpaUserRecord, userLoginStatistic);

        if(matchingUserLoginStatistic.isPresent()) {
            LOGGER.debug("Recording logout on matching UserLoginStatistic for current user UserLoginStatistic:  {}", userLoginStatistic);
            matchingUserLoginStatistic.get().setLogoutDate(DateTime.now());
            iUserLoginStatisticRepository.save(matchingUserLoginStatistic.get());
            return matchingUserLoginStatistic;
        }

        LOGGER.warn("Cannot complete logout update for UserLoginStatistic, no matching or valid candidate can be found.");
        return Optional.empty();
    }


    @Override
    public Optional<UserLoginStatistic> findMatchingUserLoginStatistic(UserRecord jpaUserRecord, UserLoginStatistic userLoginStatistic) {
        Assert.notNull(jpaUserRecord, "UserRecord cannot be null");
        Assert.notNull(jpaUserRecord.getId(), "UserRecord is non persistent");
        Assert.notNull(userLoginStatistic, "userLoginStatistic cannot be null");

        // Validate the UserLoginStatistic passed
        boolean isValidLoginStatistic = validateUserLoginStatistic(userLoginStatistic);

        // Find the UserLoginStatistic that matches what has been passed in, we want to make sure we update the correct record as logged out.
        if(isValidLoginStatistic) {
            DeviceTypeE deviceTypeE = userLoginStatistic.getDeviceTypeE();
            String deviceVersion = userLoginStatistic.getDeviceVersion();
            String deviceOS = userLoginStatistic.getDeviceOS();
            String iManiClientVersion = userLoginStatistic.getiManiClientVersion();

            UserLoginStatistic matchingUserLoginStatistic = iUserLoginStatisticRepository.findMatchingUserLoginStatistic(jpaUserRecord, deviceTypeE, deviceVersion, deviceOS, iManiClientVersion);

            if(matchingUserLoginStatistic != null) {
                LOGGER.debug("Found matching UserLoginStatistic for current user UserLoginStatistic:  {}", userLoginStatistic);
                matchingUserLoginStatistic.setLogoutDate(DateTime.now());
                iUserLoginStatisticRepository.save(matchingUserLoginStatistic);
                return Optional.of(matchingUserLoginStatistic);
            }
        }

        LOGGER.warn("No match or valid UserLoginStatistic was found");
        return Optional.empty();
    }
}

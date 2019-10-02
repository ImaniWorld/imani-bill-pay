package com.imani.bill.pay.service.user;

import com.imani.bill.pay.domain.user.UserLoginStatistic;
import com.imani.bill.pay.domain.user.UserRecord;

import java.util.Optional;

/**
 * @author manyce400
 */
public interface IUserLoginStatisticService {


    public boolean validateUserLoginStatistic(UserLoginStatistic userLoginStatistic);

    public Optional<UserLoginStatistic> recordUserLoginStatistic(UserRecord userRecord, UserLoginStatistic userLoginStatistic);

    public Optional<UserLoginStatistic> recordUserLogoutStatistic(UserRecord userRecord, UserLoginStatistic userLoginStatistic);

    public Optional<UserLoginStatistic> findMatchingUserLoginStatistic(UserRecord jpaUserRecord, UserLoginStatistic userLoginStatistic);


}

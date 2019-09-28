package com.imani.bill.pay.domain.user.repository;

import com.imani.bill.pay.domain.device.DeviceTypeE;
import com.imani.bill.pay.domain.user.UserLoginStatistic;
import com.imani.bill.pay.domain.user.UserRecord;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author manyce400
 */
@Repository
public interface IUserLoginStatisticRepository extends JpaRepository<UserLoginStatistic, Long> {


    @Query("Select userLoginStatistic From UserLoginStatistic userLoginStatistic Where userLoginStatistic.userRecord = ?1")
    public List<UserLoginStatistic> findAllUserLoginStatistic(UserRecord userRecord);


    @Query("Select userLoginStatistic From UserLoginStatistic userLoginStatistic Where userLoginStatistic.userRecord = ?1 and userLoginStatistic.deviceTypeE = ?2")
    public List<UserLoginStatistic> findAllUserLoginStatisticByDevice(UserRecord userRecord, DeviceTypeE deviceTypeE);


    @Query("Select userLoginStatistic From UserLoginStatistic userLoginStatistic Where userLoginStatistic.userRecord = ?1 and userLoginStatistic.deviceTypeE = ?2 and userLoginStatistic.loginDate = ?3")
    public List<UserLoginStatistic> findAllUserLoginStatisticByDeviceAndLoginDate(UserRecord userRecord, DeviceTypeE deviceTypeE, DateTime loginDate);


    @Query("Select userLoginStatistic From UserLoginStatistic userLoginStatistic Where userLoginStatistic.userRecord = ?1 and userLoginStatistic.deviceTypeE = ?2 and userLoginStatistic.deviceVersion = ?3 and userLoginStatistic.deviceOS = ?4 and userLoginStatistic.iManiClientVersion = ?5 and userLoginStatistic.logoutDate is NULL")
    public UserLoginStatistic findMatchingUserLoginStatistic(UserRecord userRecord, DeviceTypeE deviceTypeE, String deviceVersion, String deviceOS, String iManiClientVersion);


}
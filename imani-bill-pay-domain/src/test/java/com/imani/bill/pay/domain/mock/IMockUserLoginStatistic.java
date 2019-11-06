package com.imani.bill.pay.domain.mock;

import com.imani.bill.pay.domain.device.DeviceTypeE;
import com.imani.bill.pay.domain.user.UserLoginStatistic;
import com.imani.bill.pay.domain.user.UserRecord;
import org.joda.time.DateTime;
import org.springframework.util.Assert;

/**
 * @author manyce400
 */
public interface IMockUserLoginStatistic {

    public default UserLoginStatistic buildUserLoginStatistic(UserRecord userRecord) {
        Assert.notNull(userRecord, "UserRecord cannot be null");
        UserLoginStatistic userLoginStatistic = UserLoginStatistic.builder()
                .loginDate(DateTime.now())
                .deviceOS("IOS")
                .deviceTypeE(DeviceTypeE.IPhone)
                .iManiClientVersion("IPhone Client v1.0")
                .userRecord(userRecord)
                .build();
        return userLoginStatistic;
    }

}

package com.imani.bill.pay.service.user;

import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.UserRecordEvent;

/**
 * @author manyce400
 */
public interface IUserRecordManagerService {

    public UserRecordEvent getUserRecord(UserRecord userRecord);

    public UserRecordEvent registerUserRecord(UserRecord userRecord);

    public UserRecordEvent updateUserRecord(UserRecord userRecord);

}

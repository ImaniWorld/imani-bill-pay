package com.imani.bill.pay.service.user;


import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.UserRecordEvent;

import java.util.List;

/**
 * @author manyce400
 */
public interface IUserRecordAuthService {


    public UserRecordEvent authenticateAndLogInUserRecord(UserRecordEvent userRecordAuth);

    public UserRecordEvent authenticateAndLogOutUserRecord(UserRecordEvent userRecordAuth);

    public List<UserRecord> findAllUserRecord();

}

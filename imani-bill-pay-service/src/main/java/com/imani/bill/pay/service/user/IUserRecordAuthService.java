package com.imani.bill.pay.service.user;


import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.UserRecordAuth;

import java.util.List;

/**
 * @author manyce400
 */
public interface IUserRecordAuthService {


    public UserRecordAuth authenticateAndLogInUserRecord(UserRecordAuth userRecordAuth);

    public UserRecordAuth authenticateAndLogOutUserRecord(UserRecordAuth userRecordAuth);

    public List<UserRecord> findAllUserRecord();

}

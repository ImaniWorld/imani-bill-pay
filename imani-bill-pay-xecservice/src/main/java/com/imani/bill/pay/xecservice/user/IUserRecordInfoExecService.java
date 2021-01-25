package com.imani.bill.pay.xecservice.user;

import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.user.UserRecordLite;

import java.util.List;

/**
 * Defines functionality to provide information about Imani BillPay users.
 *
 * @author manyce400
 */
public interface IUserRecordInfoExecService {

    public void findUsersWithBusinessAffiliation(Long businessID, String userType, ExecutionResult<List<UserRecordLite>> executionResult);

}

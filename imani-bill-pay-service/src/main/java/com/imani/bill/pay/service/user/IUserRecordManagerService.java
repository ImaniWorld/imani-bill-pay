package com.imani.bill.pay.service.user;

import com.imani.bill.pay.domain.gateway.APIGatewayEvent;
import com.imani.bill.pay.domain.gateway.GenericAPIGatewayResponse;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.gateway.UserRecordRequest;
import com.imani.bill.pay.domain.execution.ExecutionResult;

/**
 * @author manyce400
 */
public interface IUserRecordManagerService {

    public APIGatewayEvent<UserRecordRequest, GenericAPIGatewayResponse> fetchUserRecord(UserRecordRequest userRecordRequest);

    public APIGatewayEvent<UserRecordRequest, GenericAPIGatewayResponse> registerUserRecord(UserRecordRequest userRecordRequest);

    public ExecutionResult registerUserRecord(UserRecord userRecord);

    public APIGatewayEvent<UserRecordRequest, GenericAPIGatewayResponse> updateUserRecord(UserRecordRequest userRecordRequest);

}

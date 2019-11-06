package com.imani.bill.pay.service.user;


import com.imani.bill.pay.domain.gateway.APIGatewayEvent;
import com.imani.bill.pay.domain.gateway.GenericAPIGatewayResponse;
import com.imani.bill.pay.domain.user.gateway.UserRecordRequest;

/**
 * @author manyce400
 */
public interface IUserRecordAuthService {


    public APIGatewayEvent<UserRecordRequest, GenericAPIGatewayResponse> authenticateAndLogInUserRecord(UserRecordRequest userRecordRequest);

    public APIGatewayEvent<UserRecordRequest, GenericAPIGatewayResponse> authenticateAndLogOutUserRecord(UserRecordRequest userRecordRequest);

}

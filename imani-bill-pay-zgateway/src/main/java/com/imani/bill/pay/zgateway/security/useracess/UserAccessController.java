package com.imani.bill.pay.zgateway.security.useracess;

import com.imani.bill.pay.domain.gateway.APIGatewayEvent;
import com.imani.bill.pay.domain.gateway.GenericAPIGatewayResponse;
import com.imani.bill.pay.domain.user.gateway.UserRecordRequest;
import com.imani.bill.pay.service.user.IUserRecordAuthService;
import com.imani.bill.pay.service.user.UserRecordAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author manyce400
 */
@RestController
@RequestMapping("/auth/user/access")
public class UserAccessController {


    @Autowired
    @Qualifier(UserRecordAuthService.SPRING_BEAN)
    private IUserRecordAuthService iUserRecordAuthService;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(UserAccessController.class);


    @PostMapping("/login")
    public APIGatewayEvent<UserRecordRequest, GenericAPIGatewayResponse> execImaniBillPayLogin(@RequestBody APIGatewayEvent<UserRecordRequest, GenericAPIGatewayResponse> apiGatewayEvent) {
        LOGGER.info("Processing Imani BillPay login request with apiGatewayEvent:=> {}", apiGatewayEvent);
        APIGatewayEvent<UserRecordRequest, GenericAPIGatewayResponse> apiGatewayEventResponse = iUserRecordAuthService.authenticateAndLogInUserRecord(apiGatewayEvent.getRequestBody().get());
        return apiGatewayEventResponse;
    }

    @PostMapping("/logout")
    public APIGatewayEvent<UserRecordRequest, GenericAPIGatewayResponse> execImaniBillPayLogout(@RequestBody APIGatewayEvent<UserRecordRequest, GenericAPIGatewayResponse> apiGatewayEvent) {
        LOGGER.info("Processing Imani BillPay logout request with apiGatewayEvent:=> {}", apiGatewayEvent);
        APIGatewayEvent<UserRecordRequest, GenericAPIGatewayResponse> apiGatewayEventResponse = iUserRecordAuthService.authenticateAndLogOutUserRecord(apiGatewayEvent.getRequestBody().get());
        return apiGatewayEventResponse;
    }

}

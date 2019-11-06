package com.imani.bill.pay.zgateway.user.record;

import com.imani.bill.pay.domain.gateway.APIGatewayEvent;
import com.imani.bill.pay.domain.gateway.GenericAPIGatewayResponse;
import com.imani.bill.pay.domain.user.gateway.UserRecordRequest;
import com.imani.bill.pay.service.user.IUserRecordManagerService;
import com.imani.bill.pay.service.user.UserRecordManagerService;
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
@RequestMapping("/userrecord/workflows/manage")
public class UserManagerController {


    @Autowired
    @Qualifier(UserRecordManagerService.SPRING_BEAN)
    private IUserRecordManagerService iUserRecordManagerService;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(UserManagerController.class);

    @PostMapping("/register")
    public APIGatewayEvent<UserRecordRequest, GenericAPIGatewayResponse> addNewUserRecord(@RequestBody APIGatewayEvent<UserRecordRequest, GenericAPIGatewayResponse> apiGatewayEvent) {
        LOGGER.info("Processing Imani BillPay request to register new user with apiGatewayEvent:=> {}", apiGatewayEvent);
        return iUserRecordManagerService.registerUserRecord(apiGatewayEvent.getRequestBody().get());
    }

}

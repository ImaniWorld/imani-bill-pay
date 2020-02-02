package com.imani.bill.pay.zgateway.user.record.registration;

import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.gateway.APIGatewayRequest;
import com.imani.bill.pay.domain.gateway.APIGatewayResponse;
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
@RequestMapping("/userrecord/registration")
public class RegistrationController {




    @Autowired
    @Qualifier(UserRecordManagerService.SPRING_BEAN)
    private IUserRecordManagerService iUserRecordManagerService;


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(RegistrationController.class);


    @PostMapping("/new")
    public APIGatewayResponse executeBillPayUserRegistration(@RequestBody APIGatewayRequest apiGatewayRequest) {
        LOGGER.info("Processing new Imani BillPay user registration with apiGatewayRequest :=>  {}", apiGatewayRequest);
        ExecutionResult executionResult = iUserRecordManagerService.registerUserRecord(apiGatewayRequest.getOnBehalfOf());
        return APIGatewayResponse.fromExecutionResult(executionResult);
    }

}

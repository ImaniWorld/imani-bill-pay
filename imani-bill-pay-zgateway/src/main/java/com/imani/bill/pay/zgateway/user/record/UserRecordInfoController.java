package com.imani.bill.pay.zgateway.user.record;

import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.gateway.APIGatewayResponse;
import com.imani.bill.pay.domain.user.UserRecordLite;
import com.imani.bill.pay.xecservice.user.IUserRecordInfoExecService;
import com.imani.bill.pay.xecservice.user.UserRecordInfoExecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author manyce400
 */
@RestController
@RequestMapping("/user/record/info")
public class UserRecordInfoController {


    @Autowired
    @Qualifier(UserRecordInfoExecService.SPRING_BEAN)
    private IUserRecordInfoExecService iUserRecordInfoExecService;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(UserRecordInfoController.class);

    @RequestMapping(value = "/with_business_affiliation/business_id/{business_id}/user_type/{user_type}", method = RequestMethod.GET)
    public APIGatewayResponse getCurrentResidentialLeaseBill(@PathVariable(value="business_id") Long businessID, @PathVariable(value="user_type") String userType) {
        LOGGER.info("Received request to find and return all Imani BillPay users by type=> {}", userType);
        ExecutionResult<List<UserRecordLite>> executionResult = new ExecutionResult<>();
        iUserRecordInfoExecService.findUsersWithBusinessAffiliation(businessID, userType, executionResult);
        return APIGatewayResponse.fromExecutionResult(executionResult);
    }


}

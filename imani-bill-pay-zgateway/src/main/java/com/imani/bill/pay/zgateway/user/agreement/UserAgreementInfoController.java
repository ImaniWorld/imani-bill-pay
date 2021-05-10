package com.imani.bill.pay.zgateway.user.agreement;

import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.gateway.APIGatewayResponse;
import com.imani.bill.pay.domain.utility.WaterServiceAgreementLite;
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
@RequestMapping("/user/agreement/info")
public class UserAgreementInfoController {


    @Autowired
    @Qualifier(UserRecordInfoExecService.SPRING_BEAN)
    private IUserRecordInfoExecService iUserRecordInfoExecService;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(UserAgreementInfoController.class);


    @RequestMapping(value = "/water/inforce/user_id/{user_id}", method = RequestMethod.GET)
    public APIGatewayResponse getCurrentResidentialLeaseBill(@PathVariable(value="user_id") Long userID) {
        LOGGER.info("Received request to find and return all water agreement for user with id => {}", userID);
        ExecutionResult<List<WaterServiceAgreementLite>> executionResult = new ExecutionResult<>();
        iUserRecordInfoExecService.findUsersWaterAgreements(userID, executionResult);
        return APIGatewayResponse.fromExecutionResult(executionResult);
    }


}

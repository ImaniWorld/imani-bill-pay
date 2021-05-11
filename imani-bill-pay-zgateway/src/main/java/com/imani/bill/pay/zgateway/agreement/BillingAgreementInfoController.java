package com.imani.bill.pay.zgateway.agreement;

import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.gateway.APIGatewayResponse;
import com.imani.bill.pay.domain.utility.WaterServiceAgreementLite;
import com.imani.bill.pay.xecservice.agreement.AgreementInfoExecService;
import com.imani.bill.pay.xecservice.agreement.IAgreementInfoExecService;
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
@RequestMapping("/billing/agreement/info")
public class BillingAgreementInfoController {


    @Autowired
    @Qualifier(UserRecordInfoExecService.SPRING_BEAN)
    private IUserRecordInfoExecService iUserRecordInfoExecService;

    @Autowired
    @Qualifier(AgreementInfoExecService.SPRING_BEAN)
    private IAgreementInfoExecService iAgreementInfoExecService;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(BillingAgreementInfoController.class);


    @RequestMapping(value = "/water/inforce/user_id/{user_id}", method = RequestMethod.GET)
    public APIGatewayResponse getUserWaterSvcAgreements(@PathVariable(value="user_id") Long userID) {
        LOGGER.info("Received request to find and return all water agreement for User with id => [{}]", userID);
        ExecutionResult<List<WaterServiceAgreementLite>> executionResult = new ExecutionResult<>();
        iUserRecordInfoExecService.findUsersWaterAgreements(userID, executionResult);
        return APIGatewayResponse.fromExecutionResult(executionResult);
    }

    @RequestMapping(value = "/water/inforce/properties/community_id/{community_id}", method = RequestMethod.GET)
    public APIGatewayResponse getCommunityWaterSvcAgreements(@PathVariable(value="community_id") Long communityID) {
        LOGGER.info("Received request to find and return all water agreement for Community with id => [{}]", communityID);

        ExecutionResult<List<WaterServiceAgreementLite>> executionResult = new ExecutionResult<>();
        iAgreementInfoExecService.findCommunityPropertiesWaterAgreements(communityID, executionResult);
        return APIGatewayResponse.fromExecutionResult(executionResult);
    }

}

package com.imani.bill.pay.zgateway.security.useracess;

import com.imani.bill.pay.domain.user.UserRecordEvent;
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
    public UserRecordEvent execImaniBillPayLogin(@RequestBody UserRecordEvent userRecordEvent) {
        LOGGER.info("Executing Imani BillPay login for userRecordEvent:=> {}", userRecordEvent);
        UserRecordEvent loginUserRecordEvent = iUserRecordAuthService.authenticateAndLogInUserRecord(userRecordEvent);
        return loginUserRecordEvent;
    }

}

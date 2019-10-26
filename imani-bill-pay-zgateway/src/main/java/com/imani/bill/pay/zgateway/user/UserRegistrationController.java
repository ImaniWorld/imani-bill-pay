package com.imani.bill.pay.zgateway.user;

import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.UserRecordEvent;
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
@RequestMapping("/userrecord/register")
public class UserRegistrationController {


    @Autowired
    @Qualifier(UserRecordManagerService.SPRING_BEAN)
    private IUserRecordManagerService iUserRecordManagerService;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(UserRegistrationController.class);

    @PostMapping("/new")
    public UserRecordEvent registerNewUserRecord(@RequestBody UserRecord userRecord) {
        LOGGER.info("Executing request to register new Imani BillPay UserRecord:=> {}", userRecord.getEmbeddedContactInfo().getEmail());
        UserRecordEvent userRecordEvent = iUserRecordManagerService.registerUserRecord(userRecord);
        return userRecordEvent;
    }

}

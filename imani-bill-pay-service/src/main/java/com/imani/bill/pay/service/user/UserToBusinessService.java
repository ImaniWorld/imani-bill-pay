package com.imani.bill.pay.service.user;

import com.imani.bill.pay.domain.business.Business;
import com.imani.bill.pay.domain.user.UserRecordLite;
import com.imani.bill.pay.domain.user.UserRecordTypeE;
import com.imani.bill.pay.domain.user.UserToBusiness;
import com.imani.bill.pay.domain.user.repository.IUserToBusinessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author manyce400
 */
@Service(UserToBusinessService.SPRING_BEAN)
public class UserToBusinessService implements IUserToBusinessService {


    @Autowired
    private IUserToBusinessRepository iUserToBusinessRepository;

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.user.UserToBusinessService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(UserToBusinessService.class);

    @Override
    public List<UserRecordLite> findUsersWithBusinessAffiliation(Business business, UserRecordTypeE userRecordTypeE) {
        Assert.notNull(business, "Business cannot be null");
        Assert.notNull(userRecordTypeE, "UserRecordTypeE cannot be null");

        LOGGER.debug("Executing business to user record mapping query");

        List<UserToBusiness> userToBusinesses = findUsersWithBusinessAffiliationMapping(business, userRecordTypeE);
        List<UserRecordLite> userRecordLites = new ArrayList<>();
        userToBusinesses.forEach(userToBusiness -> {
            userRecordLites.add(userToBusiness.getUserRecord().toUserRecordLite());
        });

        return userRecordLites;
    }

    @Override
    public List<UserToBusiness> findUsersWithBusinessAffiliationMapping(Business business, UserRecordTypeE userRecordTypeE) {
        Assert.notNull(business, "Business cannot be null");
        Assert.notNull(userRecordTypeE, "UserRecordTypeE cannot be null");
        return iUserToBusinessRepository.findUsersWithBusinessAffiliation(business, userRecordTypeE);
    }


}

package com.imani.bill.pay.service.user;

import com.imani.bill.pay.domain.business.Business;
import com.imani.bill.pay.domain.user.UserRecordLite;
import com.imani.bill.pay.domain.user.UserRecordTypeE;
import com.imani.bill.pay.domain.user.UserToBusiness;

import java.util.List;

/**
 * @author manyce400
 */
public interface IUserToBusinessService {

    public List<UserRecordLite> findUsersWithBusinessAffiliation(Business business, UserRecordTypeE userRecordTypeE);

    public List<UserToBusiness> findUsersWithBusinessAffiliationMapping(Business business, UserRecordTypeE userRecordTypeE);
}

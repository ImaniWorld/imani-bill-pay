package com.imani.bill.pay.domain.user.repository;

import com.imani.bill.pay.domain.business.Business;
import com.imani.bill.pay.domain.user.UserRecordTypeE;
import com.imani.bill.pay.domain.user.UserToBusiness;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author manyce400
 */
@Repository
public interface IUserToBusinessRepository extends JpaRepository<UserToBusiness, Long> {

    @Query("Select userToBusiness From UserToBusiness userToBusiness JOIN FETCH userToBusiness.userRecord Where userToBusiness.business = ?1 And userToBusiness.userRecord.userRecordTypeE = ?2")
    public List<UserToBusiness> findUsersWithBusinessAffiliation(Business business, UserRecordTypeE userRecordTypeE);

}
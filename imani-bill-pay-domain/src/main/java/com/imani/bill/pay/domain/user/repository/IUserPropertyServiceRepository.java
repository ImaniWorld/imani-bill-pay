package com.imani.bill.pay.domain.user.repository;

import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.UserResidencePropertyService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author manyce400
 */
@Repository
public interface IUserPropertyServiceRepository extends JpaRepository<UserResidencePropertyService, Long> {

    /**
     * Find and return all PropertyService that this user has signed up for.
     */
    @Query("Select userPropertyService From UserResidencePropertyService userPropertyService Where userPropertyService.userResidence.userRecord = ?1")
    public List<UserResidencePropertyService> findAllUserPropertyService(UserRecord userRecord);


    /**
     * Find and return all PropertyService that this user has signed up for.
     */
    @Query("Select userPropertyService From UserResidencePropertyService userPropertyService Where userPropertyService.userResidence.userRecord = ?1 and userPropertyService.active = true")
    public List<UserResidencePropertyService> findAllActiveUserPropertyService(UserRecord userRecord);
}

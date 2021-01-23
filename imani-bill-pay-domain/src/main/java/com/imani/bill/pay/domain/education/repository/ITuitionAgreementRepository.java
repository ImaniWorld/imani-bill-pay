package com.imani.bill.pay.domain.education.repository;

import com.imani.bill.pay.domain.education.TuitionAgreement;
import com.imani.bill.pay.domain.user.UserRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author manyce400
 */
@Repository
public interface ITuitionAgreementRepository extends JpaRepository<TuitionAgreement, Long> {

    // UserRecord passed in will be the user responsible for making payments on the TuitionAgreement
    @Query("Select tuitionAgreement From TuitionAgreement tuitionAgreement Where tuitionAgreement.embeddedAgreement.userRecord = ?1 and tuitionAgreement.embeddedAgreement.agreementInForce = 1")
    public List<TuitionAgreement> findParentTuitionAgreements(UserRecord userRecord);

}
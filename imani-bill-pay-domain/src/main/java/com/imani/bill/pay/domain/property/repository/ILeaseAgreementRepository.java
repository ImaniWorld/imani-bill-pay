package com.imani.bill.pay.domain.property.repository;

import com.imani.bill.pay.domain.property.LeaseAgreement;
import com.imani.bill.pay.domain.user.UserRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author manyce400
 */
@Repository
public interface ILeaseAgreementRepository extends JpaRepository<LeaseAgreement, Long> {

    @Query("Select leaseAgreement From LeaseAgreement leaseAgreement Where leaseAgreement.userRecord = ?1")
    public LeaseAgreement findUserLeaseAgreement(UserRecord userRecord);

    @Query("Select leaseAgreement From LeaseAgreement leaseAgreement Where leaseAgreement.userRecord = ?1 and leaseAgreement.agreementInEffect = true")
    public LeaseAgreement findActiveUserLeaseAgreement(UserRecord userRecord);

}

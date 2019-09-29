package com.imani.bill.pay.domain.property.repository;

import com.imani.bill.pay.domain.property.RentalAgreement;
import com.imani.bill.pay.domain.user.UserRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author manyce400
 */
@Repository
public interface IRentalAgreementRepository extends JpaRepository<RentalAgreement, Long> {

    @Query("Select rentalAgreement From RentalAgreement rentalAgreement Where rentalAgreement.userRecord = ?1")
    public RentalAgreement findUserRentalAgreement(UserRecord userRecord);

    @Query("Select rentalAgreement From RentalAgreement rentalAgreement Where rentalAgreement.userRecord = ?1 and rentalAgreement.agreementInEffect = true")
    public RentalAgreement findActiveUserRentalAgreement(UserRecord userRecord);

}

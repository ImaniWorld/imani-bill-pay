package com.imani.bill.pay.domain.billing.repository;

import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.utility.WaterServiceAgreement;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author manyce400
 */
@Repository
public interface IImaniBillWaterSvcAgreementRepository extends JpaRepository<ImaniBill, Long> {

    @Query("Select imaniBill From ImaniBill imaniBill Where imaniBill.billedUser = ?1 and imaniBill.waterServiceAgreement = ?2 and imaniBill.billScheduleDate = ?3")
    public Optional<ImaniBill> getImaniBillForAgreement(UserRecord userRecord, WaterServiceAgreement waterServiceAgreement, DateTime billScheduleDate);

    @Query("Select imaniBill From ImaniBill imaniBill Where imaniBill.waterServiceAgreement = ?1 and imaniBill.amountPaid < imaniBill.amountOwed")
    public List<ImaniBill> findAllAgreementUnPaidBills(WaterServiceAgreement waterServiceAgreement);

}

package com.imani.bill.pay.domain.billing.repository;

import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.business.Business;
import com.imani.bill.pay.domain.geographical.Community;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.utility.UtilityServiceArea;
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

    @Query("Select imaniBill From ImaniBill imaniBill Where imaniBill.waterServiceAgreement.id = ?1 and imaniBill.billScheduleDate = ?2")
    public Optional<ImaniBill> getImaniBillForAgreement(Long id, DateTime billScheduleDate);

    @Query("Select imaniBill From ImaniBill imaniBill Where imaniBill.waterServiceAgreement.id = ?1 and imaniBill.amountPaid < imaniBill.amountOwed")
    public List<ImaniBill> findAllAgreementUnPaidBills(Long id);

    @Query("Select imaniBill From ImaniBill imaniBill Where imaniBill.waterServiceAgreement = ?1 and imaniBill.amountPaid < imaniBill.amountOwed")
    public List<ImaniBill> findAllAgreementUnPaidBills(WaterServiceAgreement waterServiceAgreement);

    @Query("Select imaniBill From ImaniBill imaniBill Where imaniBill.waterServiceAgreement.embeddedAgreement.agreementUserRecord = ?1 and imaniBill.waterServiceAgreement = ?2 and imaniBill.billScheduleDate = ?3")
    public Optional<ImaniBill> getImaniBillForAgreement(UserRecord userRecord, WaterServiceAgreement waterServiceAgreement, DateTime billScheduleDate);

    @Query("Select imaniBill From ImaniBill imaniBill Where imaniBill.waterServiceAgreement.embeddedAgreement.agreementBusiness = ?1 and imaniBill.waterServiceAgreement = ?2 and imaniBill.billScheduleDate = ?3")
    public Optional<ImaniBill> getImaniBillForAgreement(Business business, WaterServiceAgreement waterServiceAgreement, DateTime billScheduleDate);

    @Query("Select imaniBill From ImaniBill imaniBill Where imaniBill.waterServiceAgreement.embeddedAgreement.agreementCommunity = ?1 and imaniBill.waterServiceAgreement = ?2 and imaniBill.billScheduleDate = ?3")
    public Optional<ImaniBill> getImaniBillForAgreement(Community community, WaterServiceAgreement waterServiceAgreement, DateTime billScheduleDate);

    @Query("Select imaniBill From ImaniBill imaniBill Where imaniBill.waterServiceAgreement.embeddedUtilityService.utilityServiceArea = ?1 and imaniBill.waterServiceAgreement = ?2 and imaniBill.billScheduleDate = ?3")
    public Optional<ImaniBill> getImaniBillForAgreement(UtilityServiceArea utilityServiceArea, WaterServiceAgreement waterServiceAgreement, DateTime billScheduleDate);

}

package com.imani.bill.pay.domain.billing.repository;

import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.business.Business;
import com.imani.bill.pay.domain.geographical.Community;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.utility.SewerServiceAgreement;
import com.imani.bill.pay.domain.utility.UtilityServiceArea;
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
public interface IImaniBillSewerSvcAgreementRepository extends JpaRepository<ImaniBill, Long> {

    @Query("Select imaniBill From ImaniBill imaniBill Where imaniBill.sewerServiceAgreement.id = ?1 and imaniBill.amountPaid < imaniBill.amountOwed")
    public List<ImaniBill> findAllAgreementUnPaidBills(Long id);

    @Query("Select imaniBill From ImaniBill imaniBill Where imaniBill.sewerServiceAgreement.embeddedAgreement.agreementUserRecord = ?1 and imaniBill.sewerServiceAgreement = ?2 and imaniBill.billScheduleDate = ?3")
    public Optional<ImaniBill> getImaniBillForAgreement(UserRecord userRecord, SewerServiceAgreement sewerServiceAgreement, DateTime billScheduleDate);

    @Query("Select imaniBill From ImaniBill imaniBill Where imaniBill.sewerServiceAgreement.embeddedAgreement.agreementBusiness = ?1 and imaniBill.sewerServiceAgreement = ?2 and imaniBill.billScheduleDate = ?3")
    public Optional<ImaniBill> getImaniBillForAgreement(Business business, SewerServiceAgreement sewerServiceAgreement, DateTime billScheduleDate);

    @Query("Select imaniBill From ImaniBill imaniBill Where imaniBill.sewerServiceAgreement.embeddedAgreement.agreementCommunity = ?1 and imaniBill.sewerServiceAgreement = ?2 and imaniBill.billScheduleDate = ?3")
    public Optional<ImaniBill> getImaniBillForAgreement(Community community, SewerServiceAgreement sewerServiceAgreement, DateTime billScheduleDate);

    @Query("Select imaniBill From ImaniBill imaniBill Where imaniBill.sewerServiceAgreement.embeddedUtilityService.utilityServiceArea = ?1 and imaniBill.sewerServiceAgreement = ?2 and imaniBill.billScheduleDate = ?3")
    public Optional<ImaniBill> getImaniBillForAgreement(UtilityServiceArea SewerServiceAgreement, SewerServiceAgreement sewerServiceAgreement, DateTime billScheduleDate);

}

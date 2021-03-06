package com.imani.bill.pay.domain.utility.repository;

import com.imani.bill.pay.domain.geographical.Community;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.utility.WaterServiceAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author manyce400
 */
@Repository
public interface IWaterServiceAgreementRepository extends JpaRepository<WaterServiceAgreement, Long> {

    @Query("Select waterServiceAgreement From WaterServiceAgreement waterServiceAgreement Where waterServiceAgreement.embeddedAgreement.agreementUserRecord = ?1")
    public Optional<WaterServiceAgreement> findWaterServiceAgreement(UserRecord userRecord);

    @Query("Select waterServiceAgreement From WaterServiceAgreement waterServiceAgreement Where waterServiceAgreement.embeddedAgreement.agreementUserRecord = ?1 and waterServiceAgreement.embeddedAgreement.agreementInForce = 1")
    public List<WaterServiceAgreement> findAllInForceWaterServiceAgreement(UserRecord userRecord);

    @Query("Select waterServiceAgreement From WaterServiceAgreement waterServiceAgreement Where waterServiceAgreement.embeddedAgreement.agreementProperty.community = ?1 and waterServiceAgreement.embeddedAgreement.agreementInForce = ?2")
    public List<WaterServiceAgreement> findAllCommunityPropertiesWaterServiceAgreement(Community community, boolean agreementInForce);

    @Query("Select waterServiceAgreement From WaterServiceAgreement waterServiceAgreement Where waterServiceAgreement.embeddedAgreement.agreementInForce = 1")
    public List<WaterServiceAgreement> findAllWhereAgreementInforce();

}

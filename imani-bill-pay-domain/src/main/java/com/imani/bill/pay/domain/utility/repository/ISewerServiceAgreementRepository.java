package com.imani.bill.pay.domain.utility.repository;

import com.imani.bill.pay.domain.utility.SewerServiceAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author manyce400
 */
@Repository
public interface ISewerServiceAgreementRepository extends JpaRepository<SewerServiceAgreement, Long> {

    @Query("Select sewerServiceAgreement From SewerServiceAgreement sewerServiceAgreement Where sewerServiceAgreement.embeddedAgreement.agreementInForce = 1")
    public List<SewerServiceAgreement> findAllWhereAgreementInforce();

}

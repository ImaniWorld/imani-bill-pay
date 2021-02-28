package com.imani.bill.pay.domain.utility.repository;

import com.imani.bill.pay.domain.utility.SewerServiceAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author manyce400
 */
@Repository
public interface ISewerServiceAgreementRepository extends JpaRepository<SewerServiceAgreement, Long> {


}

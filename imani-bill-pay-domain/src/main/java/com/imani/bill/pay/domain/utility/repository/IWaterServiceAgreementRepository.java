package com.imani.bill.pay.domain.utility.repository;

import com.imani.bill.pay.domain.utility.WaterServiceAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author manyce400
 */
@Repository
public interface IWaterServiceAgreementRepository extends JpaRepository<WaterServiceAgreement, Long> {



}
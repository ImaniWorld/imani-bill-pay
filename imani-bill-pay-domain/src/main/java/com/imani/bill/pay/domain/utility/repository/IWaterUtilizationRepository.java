package com.imani.bill.pay.domain.utility.repository;

import com.imani.bill.pay.domain.utility.WaterServiceAgreement;
import com.imani.bill.pay.domain.utility.WaterUtilization;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author manyce400
 */
@Repository
public interface IWaterUtilizationRepository extends JpaRepository<WaterUtilization, Long> {

    @Query("Select waterUtilization From WaterUtilization waterUtilization Where waterUtilization.waterServiceAgreement = ?1 and waterUtilization.createDate >= ?2 and waterUtilization.createDate <= ?3")
    public List<WaterUtilization> findUtilizationInPeriod(WaterServiceAgreement waterServiceAgreement, DateTime start, DateTime end);

    @Query("Select waterUtilization From WaterUtilization waterUtilization Where waterUtilization.waterServiceAgreement.id = ?1 and waterUtilization.createDate >= ?2 and waterUtilization.createDate <= ?3")
    public List<WaterUtilization> findUtilizationInPeriod(Long waterServiceAgreementID, DateTime start, DateTime end);

}

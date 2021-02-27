package com.imani.bill.pay.domain.utility.repository;

import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.utility.WaterUtilizationCharge;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author manyce400
 */
@Repository
public interface IWaterUtilizationChargeRepository extends JpaRepository<WaterUtilizationCharge, Long> {

    @Query("Select waterUtilizationCharge From WaterUtilizationCharge waterUtilizationCharge Where waterUtilizationCharge.imaniBill = ?1 and waterUtilizationCharge.utilizationStart = ?2 and waterUtilizationCharge.utilizationEnd = ?3")
    public Optional<WaterUtilizationCharge> findByImaniBillInQtr(ImaniBill imaniBill, DateTime atStartOfQuarter, DateTime atEndOfQuarter);

}

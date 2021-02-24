package com.imani.bill.pay.domain.utility.repository;

import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.utility.WaterUtilizationCharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author manyce400
 */
@Repository
public interface IWaterUtilizationChargeRepository extends JpaRepository<WaterUtilizationCharge, Long> {

    @Query("Select waterUtilizationCharge From WaterUtilizationCharge waterUtilizationCharge Where waterUtilizationCharge.imaniBill = ?1")
    public Optional<WaterUtilizationCharge> findByImaniBill(ImaniBill imaniBill);

}

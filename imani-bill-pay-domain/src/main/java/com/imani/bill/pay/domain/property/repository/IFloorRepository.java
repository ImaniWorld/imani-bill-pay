package com.imani.bill.pay.domain.property.repository;

import com.imani.bill.pay.domain.property.Floor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author manyce400
 */
@Repository
public interface IFloorRepository extends JpaRepository<Floor, Long> {



}

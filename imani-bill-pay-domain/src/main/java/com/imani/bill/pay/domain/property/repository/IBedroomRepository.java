package com.imani.bill.pay.domain.property.repository;

import com.imani.bill.pay.domain.property.Bedroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author manyce400
 */
@Repository
public interface IBedroomRepository extends JpaRepository<Bedroom, Long> {




}
package com.imani.bill.pay.domain.business.repository;

import com.imani.bill.pay.domain.business.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author manyce400
 */
@Repository
public interface IBusinessRepository extends JpaRepository<Business, Long> {


}

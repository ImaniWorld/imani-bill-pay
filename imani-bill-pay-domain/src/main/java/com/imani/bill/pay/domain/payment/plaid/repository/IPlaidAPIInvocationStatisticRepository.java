package com.imani.bill.pay.domain.payment.plaid.repository;

import com.imani.bill.pay.domain.payment.plaid.PlaidAPIInvocationStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author manyce400
 */
@Repository
public interface IPlaidAPIInvocationStatisticRepository extends JpaRepository<PlaidAPIInvocationStatistic, Long> {


}
package com.imani.bill.pay.domain.payment.plaid.repository;

import com.imani.bill.pay.domain.payment.plaid.PlaidAPIInvocationStatistic;
import com.imani.bill.pay.domain.property.PropertyManager;
import com.imani.bill.pay.domain.user.UserRecord;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author manyce400
 */
@Repository
public interface IPlaidAPIInvocationStatisticRepository extends JpaRepository<PlaidAPIInvocationStatistic, Long> {

    @Query("Select plaidAPIInvocationStatistic From PlaidAPIInvocationStatistic plaidAPIInvocationStatistic Where plaidAPIInvocationStatistic.userRecord = ?1 and plaidAPIInvocationStatistic.apiInvocationStartDate = ?2 and plaidAPIInvocationStatistic.apiInvocationEndDate = ?3")
    public List<PlaidAPIInvocationStatistic> findFailedAccessTokenRequestInRange(UserRecord userRecord, DateTime start, DateTime end);

    @Query("Select plaidAPIInvocationStatistic From PlaidAPIInvocationStatistic plaidAPIInvocationStatistic Where plaidAPIInvocationStatistic.propertyManager = ?1 and plaidAPIInvocationStatistic.apiInvocationStartDate = ?2 and plaidAPIInvocationStatistic.apiInvocationEndDate = ?3")
    public List<PlaidAPIInvocationStatistic> findFailedAccessTokenRequestInRange(PropertyManager propertyManager, DateTime start, DateTime end);

}
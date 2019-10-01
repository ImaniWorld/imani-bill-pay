package com.imani.bill.pay.domain.payment.repository;

import com.imani.bill.pay.domain.payment.PlaidAPIStatistic;
import com.imani.bill.pay.domain.payment.PlaidProductE;
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
public interface IPlaidAPIStatisticRepository extends JpaRepository<PlaidAPIStatistic, Long> {


    @Query("Select  plaidAPIExecMetric From PlaidAPIStatistic plaidAPIExecMetric Where plaidAPIExecMetric.userRecord =?1 and plaidAPIExecMetric.apiInvocationStartDate >= ?2 and plaidAPIExecMetric.apiInvocationStartDate <= ?3")
    public List<PlaidAPIStatistic> findUserPlaidAPIExecMetricBetweenDates(UserRecord userRecord, DateTime startDate, DateTime endDate);

    @Query("Select  plaidAPIExecMetric From PlaidAPIStatistic plaidAPIExecMetric Where plaidAPIExecMetric.userRecord =?1 and plaidAPIExecMetric.plaidProductE =?2 and plaidAPIExecMetric.apiInvocationStartDate >= ?3 and plaidAPIExecMetric.apiInvocationStartDate <= ?4")
    public List<PlaidAPIStatistic> findUserPlaidAPIExecMetricByProductBetweenDates(UserRecord userRecord, PlaidProductE plaidProductE, DateTime startDate, DateTime endDate);
}

package com.imani.bill.pay.domain.payment.repository;

import com.imani.bill.pay.domain.payment.PlaidAPIStatistic;
import com.imani.bill.pay.domain.payment.plaid.PlaidProductE;
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


    @Query("Select  plaidAPIStatistic From PlaidAPIStatistic plaidAPIStatistic Where plaidAPIStatistic.achPaymentInfo.userRecord =?1 and plaidAPIStatistic.apiInvocationStartDate >= ?2 and plaidAPIStatistic.apiInvocationStartDate <= ?3")
    public List<PlaidAPIStatistic> findUserPlaidAPIExecMetricBetweenDates(UserRecord userRecord, DateTime startDate, DateTime endDate);

    @Query("Select  plaidAPIStatistic From PlaidAPIStatistic plaidAPIStatistic Where plaidAPIStatistic.achPaymentInfo.userRecord =?1 and plaidAPIStatistic.plaidProductE =?2 and plaidAPIStatistic.apiInvocationStartDate >= ?3 and plaidAPIStatistic.apiInvocationStartDate <= ?4")
    public List<PlaidAPIStatistic> findUserPlaidAPIExecMetricByProductBetweenDates(UserRecord userRecord, PlaidProductE plaidProductE, DateTime startDate, DateTime endDate);
}

package com.imani.bill.pay.domain.billing.repository;

import com.imani.bill.pay.domain.billing.BillScheduleTypeE;
import com.imani.bill.pay.domain.billing.BillServiceRenderedTypeE;
import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.user.UserRecord;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IImaniBillRepository extends JpaRepository<ImaniBill, Long> {

    @Query("Select imaniBill From ImaniBill imaniBill Where imaniBill.billedUser = ?1 and imaniBill.billScheduleDate = ?2")
    public ImaniBill getImaniBillForScheduleDate(UserRecord userRecord, DateTime dateTime);

    @Query("Select imaniBill From ImaniBill imaniBill Where imaniBill.billedUser = ?1 and imaniBill.billScheduleDate = ?2 and imaniBill.billScheduleTypeE = ?3 and imaniBill.billServiceRenderedTypeE = ?4")
    public Optional<ImaniBill> getImaniBill(UserRecord userRecord, DateTime dateTime, BillScheduleTypeE billScheduleTypeE, BillServiceRenderedTypeE billServiceRenderedTypeE);

}
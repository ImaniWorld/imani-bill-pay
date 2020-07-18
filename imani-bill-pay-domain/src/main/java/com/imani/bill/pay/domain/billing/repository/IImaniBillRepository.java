package com.imani.bill.pay.domain.billing.repository;

import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.user.UserRecord;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IImaniBillRepository extends JpaRepository<ImaniBill, Long> {

    @Query("Select imaniBill From ImaniBill imaniBill Where imaniBill.billedUser = ?1 and imaniBill.billScheduleDate = ?2")
    public ImaniBill getImaniBillForScheduleDate(UserRecord userRecord, DateTime dateTime);

}
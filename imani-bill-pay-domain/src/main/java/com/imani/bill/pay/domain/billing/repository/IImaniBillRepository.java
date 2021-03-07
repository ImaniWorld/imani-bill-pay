package com.imani.bill.pay.domain.billing.repository;

import com.imani.bill.pay.domain.billing.ImaniBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IImaniBillRepository extends JpaRepository<ImaniBill, Long> {

//    @Query("Select imaniBill From ImaniBill imaniBill Where imaniBill.billedUser = ?1 and imaniBill.billScheduleDate = ?2")
//    public ImaniBill getImaniBillForScheduleDate(UserRecord userRecord, DateTime dateTime);

//    @Query("Select imaniBill From ImaniBill imaniBill Where imaniBill.billedUser = ?1 and imaniBill.tuitionAgreement = ?2")
//    public Optional<ImaniBill> getImaniBillForTuitionAndUser(UserRecord userRecord, TuitionAgreement tuitionAgreement);

//    @Query("Select imaniBill From ImaniBill imaniBill Where imaniBill.billedUser = ?1 and imaniBill.billScheduleDate = ?2 and imaniBill.billScheduleTypeE = ?3 and imaniBill.billServiceRenderedTypeE = ?4")
//    public Optional<ImaniBill> getImaniBill(UserRecord userRecord, DateTime dateTime, BillScheduleTypeE billScheduleTypeE, BillServiceRenderedTypeE billServiceRenderedTypeE);

//    @Query("Select imaniBill From ImaniBill imaniBill Where imaniBill.billedUser = ?1 and imaniBill.billScheduleDate = ?2 and imaniBill.billScheduleTypeE = ?3 and imaniBill.billServiceRenderedTypeE = ?4")
//    public Optional<ImaniBill> getImaniBillFetchRecords(UserRecord userRecord, DateTime dateTime, BillScheduleTypeE billScheduleTypeE, BillServiceRenderedTypeE billServiceRenderedTypeE);

//    @Query("Select imaniBill From ImaniBill imaniBill LEFT JOIN FETCH imaniBill.imaniBillPayRecords Where imaniBill.billedUser = ?1 and imaniBill.billScheduleDate >= ?2 and imaniBill.billScheduleDate <= ?3 and imaniBill.billScheduleTypeE = ?4 and imaniBill.billServiceRenderedTypeE = ?5")
//    public Set<ImaniBill> getYTDImaniBillsFetchRecords(UserRecord userRecord, DateTime start, DateTime end, BillScheduleTypeE billScheduleTypeE, BillServiceRenderedTypeE billServiceRenderedTypeE);


//    @Query("Select imaniBill From ImaniBill imaniBill Where imaniBill.billedUser = ?1 and imaniBill.billScheduleDate >= ?2 and imaniBill.billScheduleDate <= ?3 and imaniBill.tuitionAgreement = ?4 and imaniBill.amountPaid < imaniBill.amountOwed")
//    public Set<ImaniBill> getYTDUnPaidImaniBillsForUser(UserRecord userRecord, DateTime start, DateTime end, TuitionAgreement tuitionAgreement);

}
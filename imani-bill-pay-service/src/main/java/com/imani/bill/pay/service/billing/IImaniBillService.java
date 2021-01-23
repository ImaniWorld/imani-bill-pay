package com.imani.bill.pay.service.billing;

import com.imani.bill.pay.domain.billing.BillServiceRenderedTypeE;
import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.education.TuitionAgreement;
import com.imani.bill.pay.domain.user.UserRecord;

import java.util.Optional;
import java.util.Set;

/**
 * @author manyce400
 */
public interface IImaniBillService {

    public Optional<ImaniBill> findByID(Long id);

    public Optional<ImaniBill> findByUserCurrentMonthBill(UserRecord userRecord, BillServiceRenderedTypeE billServiceRenderedTypeE);

    public Set<ImaniBill> findYTDResidentialPropertyLeaseBills(UserRecord userRecord);

    public Set<ImaniBill> findYTDUnPaidImaniBillsForUser(UserRecord userRecord, TuitionAgreement tuitionAgreement);

    public Optional<ImaniBill> findCurrentMonthBillForTuitionAgreement(UserRecord userRecord, TuitionAgreement tuitionAgreement);

    public void save(ImaniBill imaniBill);

}

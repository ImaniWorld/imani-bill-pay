package com.imani.bill.pay.service.education;

import com.imani.bill.pay.domain.education.TuitionAgreement;

/**
 * @author manyce400
 */
public interface ITuitionAgreementService {

//    public void newTuitionAgreement(TuitionAgreementLite tuitionAgreementLite);

    public Integer findNumberOfDaysTillPaymentLate(TuitionAgreement tuitionAgreement);

}
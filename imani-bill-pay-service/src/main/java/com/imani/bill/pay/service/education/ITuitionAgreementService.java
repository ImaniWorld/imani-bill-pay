package com.imani.bill.pay.service.education;

import com.imani.bill.pay.domain.education.TuitionAgreement;
import com.imani.bill.pay.domain.education.TuitionAgreementLite;

/**
 * @author manyce400
 */
public interface ITuitionAgreementService {

    public Integer findNumberOfDaysTillPaymentLate(TuitionAgreement tuitionAgreement);

    public void newTuitionAgreement(TuitionAgreementLite tuitionAgreementLite);

}
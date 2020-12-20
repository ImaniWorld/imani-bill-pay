package com.imani.bill.pay.service.payment.record;

import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.payment.record.ImaniBillPayRecord;

/**
 * @author manyce400
 */
public interface IImaniBillPayRecordService {

    public Double getTotalAmountPending(ImaniBill imaniBill);

    public void finalizeBillPayment(ImaniBillPayRecord imaniBillPayRecord, ImaniBill imaniBill);

}

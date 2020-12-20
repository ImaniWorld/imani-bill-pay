package com.imani.bill.pay.service.payment.record;

import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.billing.repository.IImaniBillRepository;
import com.imani.bill.pay.domain.payment.EmbeddedPayment;
import com.imani.bill.pay.domain.payment.PaymentStatusE;
import com.imani.bill.pay.domain.payment.record.ImaniBillPayRecord;
import com.imani.bill.pay.domain.payment.record.repository.ImaniBillPayRecordRepository;
import com.imani.bill.pay.domain.payment.repository.IACHPaymentInfoRepository;
import com.imani.bill.pay.service.util.DateTimeUtil;
import com.imani.bill.pay.service.util.IDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author manyce400
 */
@Service(ImaniBillPayRecordService.SPRING_BEAN)
public class ImaniBillPayRecordService implements IImaniBillPayRecordService {

    @Autowired
    private IImaniBillRepository iImaniBillRepository;

    @Autowired
    private ImaniBillPayRecordRepository imaniBillPayRecordRepository;

    @Autowired
    private IACHPaymentInfoRepository iachPaymentInfoRepository;

    @Autowired
    @Qualifier(DateTimeUtil.SPRING_BEAN)
    private IDateTimeUtil iDateTimeUtil;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ImaniBillPayRecordService.class);

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.payment.record.ImaniBillPayHistoryService";



    @Override
    public Double getTotalAmountPending(ImaniBill imaniBill) {
        Assert.notNull(imaniBill, "ImaniBill cannot be null");

        LOGGER.info("Computing total amount on pending payments for ImaniBill ID:> {}", imaniBill.getId());

        List<ImaniBillPayRecord> imaniBillPayHistories = imaniBillPayRecordRepository.findPaymentHistoryOnBillByStatus(imaniBill, PaymentStatusE.Pending);
        double totalPending = 0;
        for(ImaniBillPayRecord imaniBillPayRecord : imaniBillPayHistories) {
            totalPending = totalPending + imaniBillPayRecord.getEmbeddedPayment().getPaymentAmount();
        }

        return totalPending;
    }

    @Transactional
    @Override
    public void finalizeBillPayment(ImaniBillPayRecord imaniBillPayRecord, ImaniBill imaniBill) {
        Assert.notNull(imaniBillPayRecord, "ImaniBillPayRecord cannot be null");
        Assert.notNull(imaniBill, "ImaniBill cannot be null");

        LOGGER.debug("Recording BillPay transaction...");

        // Figure out the details of the embedded payment
        EmbeddedPayment embeddedPayment = imaniBillPayRecord.getEmbeddedPayment();
        PaymentStatusE paymentStatusE = embeddedPayment.getPaymentStatusE();

        switch (paymentStatusE) {
            case Success:
                imaniBill.setAmountPaid(embeddedPayment.getPaymentAmount());
            default:
                break;
        }

        imaniBillPayRecordRepository.save(imaniBillPayRecord);
        iImaniBillRepository.save(imaniBill);
    }

}
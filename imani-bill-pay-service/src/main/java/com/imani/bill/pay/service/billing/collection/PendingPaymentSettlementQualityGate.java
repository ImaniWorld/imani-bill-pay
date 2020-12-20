package com.imani.bill.pay.service.billing.collection;

import com.imani.bill.pay.domain.billing.BillingDetail;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.execution.ValidationAdvice;
import com.imani.bill.pay.domain.payment.PaymentProcessingIssueTypeE;
import com.imani.bill.pay.domain.payment.record.ImaniBillPayIssueRecord;
import com.imani.bill.pay.domain.payment.record.ImaniBillPayRecord;
import com.imani.bill.pay.service.payment.record.IImaniBillPayRecordService;
import com.imani.bill.pay.service.payment.record.ImaniBillPayRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author manyce400
 */
@Order(1)
@Service(PendingPaymentSettlementQualityGate.SPRING_BEAN)
public class PendingPaymentSettlementQualityGate implements IPaymentSettlementQualityGate {


    @Autowired
    @Qualifier(ImaniBillPayRecordService.SPRING_BEAN)
    private IImaniBillPayRecordService imaniBillPayRecordService;

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.billing.collection.PendingPaymentsQualityGate";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PendingPaymentSettlementQualityGate.class);

    @Override
    public void vetQuality(ExecutionResult<BillingDetail> executionResult, ImaniBillPayRecord imaniBillPayRecord) {
        Assert.notNull(executionResult, "executionResult cannot be null");
        Assert.notNull(imaniBillPayRecord, "imaniBillPayRecord cannot be null");

        BillingDetail billingDetail = executionResult.getResult().get();

        // Enforce:  We won't allow the user to make any payments while they still have pending payments which havent posted
        LOGGER.info("Pending Payments Quality Gate:=> Executing pending payments check on ImaniBill:=> {}", billingDetail.getImaniBill().getId());
        Double totalBillPayAmountPending = imaniBillPayRecordService.getTotalAmountPending(billingDetail.getImaniBill());

        if(totalBillPayAmountPending.doubleValue() > 0) {
            LOGGER.info("Pending Payments Quality Gate:=> Failed. Payment amount of {} still pending", totalBillPayAmountPending);
            StringBuffer sb = new StringBuffer("You have a pending payment for: ")
                    .append(totalBillPayAmountPending).append(" which needs to clear first");
            executionResult.addValidationAdvice(ValidationAdvice.newInstance(sb.toString()));

            // Record this as an issue in ImaniBillPayIssueRecord
            ImaniBillPayIssueRecord imaniBillPayIssueRecord = ImaniBillPayIssueRecord.builder()
                    .imaniBillPayRecord(imaniBillPayRecord)
                    .issueMessage(sb.toString())
                    .paymentProcessingIssueTypeE(PaymentProcessingIssueTypeE.Pending_Payments)
                    .build();
            imaniBillPayRecord.addImaniBillPayIssueRecord(imaniBillPayIssueRecord);
        }
    }

}

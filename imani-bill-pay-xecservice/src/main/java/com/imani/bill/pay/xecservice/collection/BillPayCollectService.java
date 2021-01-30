package com.imani.bill.pay.xecservice.collection;

import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.billing.ImaniBillExplained;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.payment.EmbeddedPayment;
import com.imani.bill.pay.domain.payment.PaymentStatusE;
import com.imani.bill.pay.domain.payment.record.ImaniBillPayRecord;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.service.payment.record.IImaniBillPayRecordService;
import com.imani.bill.pay.service.payment.record.ImaniBillPayRecordService;
import com.imani.bill.pay.service.payment.stripe.IStripeChargeService;
import com.imani.bill.pay.service.payment.stripe.StripeChargeService;
import com.stripe.model.Charge;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * @author manyce400
 */
@Service(BillPayCollectService.SPRING_BEAN)
public class BillPayCollectService implements IBillPayCollectService {


    @Autowired
    @Qualifier(StripeChargeService.SPRING_BEAN)
    private IStripeChargeService iStripeChargeService;

    @Autowired
    @Qualifier(ImaniBillPayRecordService.SPRING_BEAN)
    private IImaniBillPayRecordService imaniBillPayRecordService;

    public static final String SPRING_BEAN = "com.imani.bill.pay.xecservice.collection.BillPayCollectService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(BillPayCollectService.class);

    @Override
    public void processPayment(ImaniBillPayRecord imaniBillPayRecord, ExecutionResult<ImaniBillExplained> paymentExecutionResult) {
        Assert.notNull(imaniBillPayRecord, "ImaniBillPayRecord cannot be null");
        Assert.notNull(paymentExecutionResult, "ExecutionResult cannot be null");

        UserRecord userRecord = imaniBillPayRecord.getImaniBill().getBilledUser();
        ImaniBill imaniBill = imaniBillPayRecord.getImaniBill();
        ImaniBillExplained paymentDetails = paymentExecutionResult.getResult().get();

        if(!paymentExecutionResult.hasValidationAdvice()
                && !paymentExecutionResult.hasExecutionError()) {
            LOGGER.info("Executing Bill payment for User[{}] on rendered Service[{}]", userRecord.getEmbeddedContactInfo().getEmail(), imaniBill.getBillServiceRenderedTypeE());

            Optional<Charge> charge = iStripeChargeService.createCustomerACHCharge(userRecord, paymentDetails.getAmtBeingPaid());
            if(charge.isPresent()
                    && (charge.get().getStatus().equals("succeeded") || charge.get().getStatus().equals("pending"))) {
                LOGGER.info("Stripe payment has been processed successfully");
                EmbeddedPayment embeddedPayment = EmbeddedPayment.builder()
                        .paymentAmount(paymentDetails.getAmtBeingPaid())
                        .currency("USD")
                        .paymentDate(DateTime.now())
                        .paymentPostDate(DateTime.now())
                        .paymentStatusE(PaymentStatusE.Success)
                        .build();
                paymentDetails.addEmbeddedPayment(embeddedPayment);
                imaniBillPayRecord.setEmbeddedPayment(embeddedPayment);
            }
        } else {
            LOGGER.warn("***  Unable to process and book payment. Check payment details...  ***");
            EmbeddedPayment embeddedPayment = EmbeddedPayment.builder()
                    .paymentAmount(paymentDetails.getAmtBeingPaid())
                    .currency("USD")
                    .paymentDate(DateTime.now())
                    .paymentStatusE(PaymentStatusE.CannotProcess)
                    .build();
            paymentDetails.addEmbeddedPayment(embeddedPayment);
            imaniBillPayRecord.setEmbeddedPayment(embeddedPayment);
        }

        imaniBillPayRecordService.finalizeBillPayment(imaniBillPayRecord, imaniBill);
    }

}
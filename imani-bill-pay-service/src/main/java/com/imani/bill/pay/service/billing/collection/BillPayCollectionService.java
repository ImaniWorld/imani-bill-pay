package com.imani.bill.pay.service.billing.collection;

import com.imani.bill.pay.domain.billing.BillingDetail;
import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.billing.ImaniBillExplained;
import com.imani.bill.pay.domain.billing.repository.IImaniBillRepository;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.payment.ACHPaymentInfo;
import com.imani.bill.pay.domain.payment.EmbeddedPayment;
import com.imani.bill.pay.domain.payment.PaymentStatusE;
import com.imani.bill.pay.domain.payment.record.ImaniBillPayRecord;
import com.imani.bill.pay.domain.payment.repository.IACHPaymentInfoRepository;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.repository.IUserRecordRepository;
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

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * @author manyce400
 */
@Service(BillPayCollectionService.SPRING_BEAN)
public class BillPayCollectionService implements IBillPayCollectionService {


    @Autowired
    private List<IPaymentSettlementQualityGate> iPaymentSettlementQualityGateList;

    @Autowired
    private IImaniBillRepository imaniBillRepository;

    @Autowired
    private IUserRecordRepository iUserRecordRepository;

    @Autowired
    private IACHPaymentInfoRepository iachPaymentInfoRepository;

    @Autowired
    @Qualifier(StripeChargeService.SPRING_BEAN)
    private IStripeChargeService iStripeChargeService;

    @Autowired
    @Qualifier(ImaniBillPayRecordService.SPRING_BEAN)
    private IImaniBillPayRecordService imaniBillPayRecordService;

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.billing.collection.BillPayCollectionService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(BillPayCollectionService.class);


    @Transactional
    @Override
    public ExecutionResult<ImaniBillExplained> collectPayment(ImaniBillExplained imaniBillExplained) {
        Assert.notNull(imaniBillExplained, "ImaniBillExplained cannot be null");
        Assert.notNull(imaniBillExplained.getAmtBeingPaid(), "Amount to be paid cannot be null");
        Assert.notNull(imaniBillExplained.getImaniBillID(), "ImaniBill id cannot be null");

        ExecutionResult<ImaniBillExplained> billExplainedExecutionResult = new ExecutionResult<>(imaniBillExplained);

        // Build BillingDetail object
        ImaniBill imaniBill = imaniBillRepository.getOne(imaniBillExplained.getImaniBillID());
        imaniBillExplained.setAmountOwed(imaniBill.getAmountOwed());
        UserRecord userRecord = iUserRecordRepository.findByUserEmail(imaniBillExplained.getUserRecordLite().getEmail());
        ACHPaymentInfo achPaymentInfo = iachPaymentInfoRepository.findPrimaryUserACHPaymentInfo(userRecord);

        // Build a new ImaniBillPayRecord
        ImaniBillPayRecord imaniBillPayRecord = ImaniBillPayRecord.builder()
                .imaniBill(imaniBill)
                .achPaymentInfo(achPaymentInfo)
                .build();

        BillingDetail billingDetail = BillingDetail.builder()
                .userRecord(userRecord)
                .achPaymentInfo(achPaymentInfo)
                .imaniBill(imaniBill)
                .imaniBillExplained(imaniBillExplained)
                .build();

        ExecutionResult<BillingDetail> billingDetailExecutionResult = new ExecutionResult<>(billingDetail);

        LOGGER.info("Attempting to collect payment in amount => {} on ImaniBill with ID: {}", imaniBillExplained.getAmtBeingPaid(), imaniBill.getId());

        // Execute all Quality Gate checks
        iPaymentSettlementQualityGateList.forEach(iPaymentSettlementQualityGate -> {
            LOGGER.debug("Executing payment collection quality gate: {}", iPaymentSettlementQualityGate);
            iPaymentSettlementQualityGate.vetQuality(billingDetailExecutionResult, imaniBillPayRecord);
        });

        // Copy over all validation failures and errors and other properties
        billExplainedExecutionResult.addValidationAdvices(billingDetailExecutionResult.getValidationAdvices());
        billExplainedExecutionResult.addExecutionErrors(billingDetailExecutionResult.getExecutionErrors());
        billExplainedExecutionResult.addPlatformActionRequiredESet(billingDetailExecutionResult.getPlatformActionRequiredSet());


        if(!billingDetailExecutionResult.hasValidationAdvice()
                && !billingDetailExecutionResult.hasExecutionError()) {
            LOGGER.info("Executing ImaniBill payment for user=> {} for rendered service=> {}", imaniBillExplained.getUserRecordLite().getEmail(), imaniBillExplained.getBillServiceRenderedTypeE());
            Optional<Charge> charge = iStripeChargeService.createCustomerACHCharge(userRecord, imaniBillExplained.getAmtBeingPaid());
            onPaymentProcessed(imaniBillExplained, imaniBillPayRecord);
        } else {
            onPaymentFailed(imaniBillExplained, imaniBillPayRecord);
        }

        LOGGER.info("Finalizing bill payment collection...");
        imaniBillPayRecordService.finalizeBillPayment(imaniBillPayRecord, imaniBill);
        return billExplainedExecutionResult;
    }


    private void onPaymentProcessed(ImaniBillExplained imaniBillExplained, ImaniBillPayRecord imaniBillPayRecord) {
        EmbeddedPayment embeddedPayment = EmbeddedPayment.builder()
                .paymentAmount(imaniBillExplained.getAmtBeingPaid())
                .currency("USD")
                .paymentDate(DateTime.now())
                .paymentPostDate(DateTime.now())
                .paymentStatusE(PaymentStatusE.Success)
                .build();
        imaniBillExplained.addEmbeddedPayment(embeddedPayment);
        imaniBillPayRecord.setEmbeddedPayment(embeddedPayment);
    }

    private void onPaymentFailed(ImaniBillExplained imaniBillExplained, ImaniBillPayRecord imaniBillPayRecord) {
        EmbeddedPayment embeddedPayment = EmbeddedPayment.builder()
                .paymentAmount(imaniBillExplained.getAmtBeingPaid())
                .currency("USD")
                .paymentDate(DateTime.now())
                .paymentPostDate(DateTime.now())
                .paymentStatusE(PaymentStatusE.CannotProcess)
                .build();
        imaniBillExplained.addEmbeddedPayment(embeddedPayment);
        imaniBillPayRecord.setEmbeddedPayment(embeddedPayment);
    }

}

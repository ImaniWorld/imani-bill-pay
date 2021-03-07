package com.imani.bill.pay.xecservice.collection;

import com.imani.bill.pay.domain.APICallTimeRecord;
import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.billing.ImaniBillExplained;
import com.imani.bill.pay.domain.billing.repository.IImaniBillRepository;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.execution.ValidationAdvice;
import com.imani.bill.pay.domain.payment.ACHPaymentInfo;
import com.imani.bill.pay.domain.payment.PaymentProcessingIssueTypeE;
import com.imani.bill.pay.domain.payment.plaid.*;
import com.imani.bill.pay.domain.payment.record.ImaniBillPayIssueRecord;
import com.imani.bill.pay.domain.payment.record.ImaniBillPayPlaidRecord;
import com.imani.bill.pay.domain.payment.record.ImaniBillPayRecord;
import com.imani.bill.pay.domain.payment.repository.IACHPaymentInfoRepository;
import com.imani.bill.pay.domain.platform.PlatformActionRequiredE;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.repository.IUserRecordRepository;
import com.imani.bill.pay.service.payment.IPlaidAccountBalanceService;
import com.imani.bill.pay.service.payment.PlaidAccountBalanceService;
import com.imani.bill.pay.service.payment.plaid.IPlaidItemService;
import com.imani.bill.pay.service.payment.plaid.PlaidItemServiceImpl;
import com.imani.bill.pay.service.payment.record.IImaniBillPayRecordService;
import com.imani.bill.pay.service.payment.record.ImaniBillPayRecordService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author manyce400
 */
@Aspect
@Component
public class BillPayCollectServiceAdvisor {


    @Autowired
    private IUserRecordRepository iUserRecordRepository;

    @Autowired
    private IImaniBillRepository imaniBillRepository;

    @Autowired
    private IACHPaymentInfoRepository iACHPaymentInfoRepository;

    @Autowired
    @Qualifier(ImaniBillPayRecordService.SPRING_BEAN)
    private IImaniBillPayRecordService imaniBillPayRecordService;

    @Autowired
    @Qualifier(PlaidItemServiceImpl.SPRING_BEAN)
    private IPlaidItemService iPlaidItemService;

    @Autowired
    @Qualifier(PlaidAccountBalanceService.SPRING_BEAN)
    private IPlaidAccountBalanceService iPlaidAccountBalanceService;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(BillPayCollectServiceAdvisor.class);

    /**
     * Executes a Before advice before {@linkplain com.imani.bill.pay.xecservice.collection.BillPayCollectService#processPayment(ImaniBillPayRecord, ExecutionResult)}   }
     */
    @Before(value = "execution(* com.imani.bill.pay.xecservice.collection.BillPayCollectService.processPayment(..)) and args(imaniBillPayRecord, paymentExecutionResult)")
    public void beforeAdvice(JoinPoint joinPoint, ImaniBillPayRecord imaniBillPayRecord, ExecutionResult<ImaniBillExplained> paymentExecutionResult) {
        LOGGER.debug("Executing BillPay collection advice.....");

        ImaniBillExplained paymentDetails = paymentExecutionResult.getResult().get();
        UserRecord userRecord = iUserRecordRepository.findByUserEmail(paymentDetails.getUserBilled().getEmail());
        Optional<ImaniBill> imaniBill = imaniBillRepository.findById(paymentDetails.getImaniBillID());

        if (userRecord != null && imaniBill.isPresent()) {
            ACHPaymentInfo achPaymentInfo = iACHPaymentInfoRepository.findPrimaryUserACHPaymentInfo(userRecord);
            imaniBillPayRecord.setImaniBill(imaniBill.get());
            imaniBillPayRecord.setAchPaymentInfo(achPaymentInfo);

            boolean plaidItemValid = validatePlaidItemStatus(achPaymentInfo, imaniBillPayRecord, paymentExecutionResult);
            if (plaidItemValid) {
                pendingPaymentsAdvice(imaniBillPayRecord, paymentExecutionResult);
                bankBalanceAdvice(paymentDetails.getAmtBeingPaid(), imaniBillPayRecord, paymentExecutionResult);
            }
        }
    }

    void pendingPaymentsAdvice(ImaniBillPayRecord imaniBillPayRecord, ExecutionResult<ImaniBillExplained> paymentExecutionResult) {
        Double totalBillPayAmountPending = imaniBillPayRecordService.getTotalAmountPending(imaniBillPayRecord.getImaniBill());

        if(totalBillPayAmountPending.doubleValue() > 0) {
            LOGGER.info("Pending Payments Quality Gate:=> Failed. Payment amount of {} still pending", totalBillPayAmountPending);
            StringBuffer sb = new StringBuffer("Pending payment for: ")
                    .append(totalBillPayAmountPending).append(" needs to clear first.");
            paymentExecutionResult.addValidationAdvice(ValidationAdvice.newInstance(sb.toString()));

            // Record this as an issue in ImaniBillPayIssueRecord
            ImaniBillPayIssueRecord imaniBillPayIssueRecord = ImaniBillPayIssueRecord.builder()
                    .imaniBillPayRecord(imaniBillPayRecord)
                    .issueMessage(sb.toString())
                    .paymentProcessingIssueTypeE(PaymentProcessingIssueTypeE.Pending_Payments)
                    .build();
            imaniBillPayRecord.addImaniBillPayIssueRecord(imaniBillPayIssueRecord);
        }
    }

    boolean validatePlaidItemStatus(ACHPaymentInfo achPaymentInfo, ImaniBillPayRecord imaniBillPayRecord, ExecutionResult<ImaniBillExplained> paymentExecutionResult) {
        APICallTimeRecord apiCallTimeRecord = APICallTimeRecord.start();
        Optional<PlaidItemDetail> plaidItemDetail = iPlaidItemService.getPlaidItemDetail(achPaymentInfo);
        apiCallTimeRecord.endApiCall();

        if(plaidItemDetail.isPresent()) {
            PlaidAPIError plaidAPIError = plaidItemDetail.get().getPlaidItem().getError();
            PlaidErrorTypeE plaidErrorTypeE = plaidAPIError.getErrorType();
            PlaidErrorCodeE plaidErrorCodeE = plaidAPIError.getErrorCode();


            if(plaidErrorTypeE == PlaidErrorTypeE.ITEM_ERROR) {
                if(plaidErrorCodeE == PlaidErrorCodeE.ITEM_LOGIN_REQUIRED) {
                    LOGGER.warn("Plaid Item Login Quality Gate:=> Failed. Detected that this user's current Plaid link to Bank account requires a new login");
                    paymentExecutionResult.addValidationAdvice(ValidationAdvice.newInstance("Unable to validate your Bank Account Credentials."));

                    // Create BillPayPlaidRecord to track this issue
                    ImaniBillPayPlaidRecord imaniBillPayPlaidRecord = ImaniBillPayPlaidRecord.builder()
                            .plaidAPIInvocationE(PlaidAPIInvocationE.ItemGet)
                            .plaidAPIError(plaidAPIError)
                            .imaniBillPayRecord(imaniBillPayRecord)
                            .withAPICallTimeRecord(apiCallTimeRecord)
                            .build();

                    imaniBillPayRecord.addBillPayPlaidRecord(imaniBillPayPlaidRecord);

                    // Add an action item for user to be forced to relogin to their Plaid Bank account
                    LOGGER.info("Adding Platform Action for user to relogin to Plaid Bank Account link...");
                    paymentExecutionResult.addPlatformActionRequiredE(PlatformActionRequiredE.User_Plaid_Account_Login);
                    return false;
                }
            }
        }

        return true;
    }

    void bankBalanceAdvice(Double amtBeingPaid, ImaniBillPayRecord imaniBillPayRecord, ExecutionResult<ImaniBillExplained> paymentExecutionResult) {
//        UserRecord userRecord = imaniBillPayRecord.getImaniBill().getBilledUser();
//
//        APICallTimeRecord apiCallTimeRecord = APICallTimeRecord.start();
//        Optional<PlaidBankAcctBalance> balance = iPlaidAccountBalanceService.getACHPaymentInfoBalances(userRecord);
//        apiCallTimeRecord.endApiCall();
//
//        System.out.println("\nbalance = " + balance + "\n");
//
//        if(balance.isPresent() && !balance.get().hasAvailableBalanceForPayment(amtBeingPaid)) {
//            Double acctBalance = balance.get().getAvailable();
//            LOGGER.info("Matching account available balance is not enough to cover transaction amount");
//            StringBuffer sb = new StringBuffer("You available account balance: ")
//                    .append(acctBalance)
//                    .append(" is not enough to cover payment");
//            paymentExecutionResult.addValidationAdvice(ValidationAdvice.newInstance(sb.toString()));
//        } else {
//            LOGGER.warn("Failed to retrieve bank account balance for user: {}", userRecord.getEmbeddedContactInfo().getEmail());
//            StringBuffer sb = new StringBuffer("Imani BillPay was unable to verify your available balance.");
//            paymentExecutionResult.addExecutionError(ExecutionError.of(sb.toString()));
//        }
    }

}
package com.imani.bill.pay.service.billing.collection;

import com.imani.bill.pay.domain.APICallTimeRecord;
import com.imani.bill.pay.domain.billing.BillingDetail;
import com.imani.bill.pay.domain.execution.ExecutionError;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.execution.ValidationAdvice;
import com.imani.bill.pay.domain.payment.plaid.PlaidBankAcctBalance;
import com.imani.bill.pay.domain.payment.record.ImaniBillPayRecord;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.service.payment.IPlaidAccountBalanceService;
import com.imani.bill.pay.service.payment.PlaidAccountBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * @author manyce400
 */
@Order(3)
public class PlaidBankBalancePaymentSettlementQualityGate implements IPaymentSettlementQualityGate {


    @Autowired
    @Qualifier(PlaidAccountBalanceService.SPRING_BEAN)
    private IPlaidAccountBalanceService iPlaidAccountBalanceService;

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.billing.collection.PlaidBankBalanceQualityGate";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PlaidBankBalancePaymentSettlementQualityGate.class);


    @Override
    public void vetQuality(ExecutionResult<BillingDetail> executionResult, ImaniBillPayRecord imaniBillPayRecord) {
        Assert.notNull(executionResult, "executionResult cannot be null");
        Assert.notNull(imaniBillPayRecord, "imaniBillPayRecord cannot be null");

        // IF Plaid ITEM Login errors were found, skip trying to execute the balance check as it wont work.
        if (!imaniBillPayRecord.hasPlaidItemLoginError()) {
            BillingDetail billingDetail = executionResult.getResult().get();
            UserRecord userRecord = billingDetail.getUserRecord();

            APICallTimeRecord apiCallTimeRecord = APICallTimeRecord.start();
            Optional<PlaidBankAcctBalance> balance = iPlaidAccountBalanceService.getACHPaymentInfoBalances(userRecord);
            apiCallTimeRecord.endApiCall();

            System.out.println("\nbalance = " + balance + "\n");

            if(balance.isPresent() && !balance.get().hasAvailableBalanceForPayment(billingDetail.getImaniBillExplained().getAmtBeingPaid())) {
                Double acctBalance = balance.get().getAvailable();
                LOGGER.info("Matching account available balance is not enough to cover transaction amount");
                StringBuffer sb = new StringBuffer("You available account balance: ")
                        .append(acctBalance)
                        .append(" is not enough to cover payment");
                executionResult.addValidationAdvice(ValidationAdvice.newInstance(sb.toString()));
            } else {
                LOGGER.warn("Failed to retrieve bank account balance for user: {}", userRecord.getEmbeddedContactInfo().getEmail());
                StringBuffer sb = new StringBuffer("Imani BillPay was unable to verify your available balance.");
                executionResult.addExecutionError(ExecutionError.of(sb.toString()));
            }
        }
    }

}

package com.imani.bill.pay.service.billing.collection;

import com.imani.bill.pay.domain.APICallTimeRecord;
import com.imani.bill.pay.domain.billing.BillingDetail;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.execution.ValidationAdvice;
import com.imani.bill.pay.domain.payment.plaid.*;
import com.imani.bill.pay.domain.payment.record.ImaniBillPayPlaidRecord;
import com.imani.bill.pay.domain.payment.record.ImaniBillPayRecord;
import com.imani.bill.pay.domain.platform.PlatformActionRequiredE;
import com.imani.bill.pay.service.payment.plaid.IPlaidItemService;
import com.imani.bill.pay.service.payment.plaid.PlaidItemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * @author manyce400
 */
@Order(2)
@Service(PlaidItemPaymentSettlementQualityGate.SPRING_BEAN)
class PlaidItemPaymentSettlementQualityGate implements IPaymentSettlementQualityGate {


    @Autowired
    @Qualifier(PlaidItemServiceImpl.SPRING_BEAN)
    private IPlaidItemService iPlaidItemService;

    public static String PLAID_ITEM_CREDENTIALS_ERROR = "";

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.billing.collection.PlaidItemQualityGate";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PlaidItemPaymentSettlementQualityGate.class);


    @Override
    public void vetQuality(ExecutionResult<BillingDetail> executionResult, ImaniBillPayRecord imaniBillPayRecord) {
        Assert.notNull(executionResult, "executionResult cannot be null");
        Assert.notNull(imaniBillPayRecord, "imaniBillPayRecord cannot be null");

        BillingDetail billingDetail = executionResult.getResult().get();

        LOGGER.info("Plaid Item Login Quality Gate:=> Executing Plaid Item Login quality gate check for User: {}", billingDetail.getUserRecord().getEmbeddedContactInfo().getEmail());

        APICallTimeRecord apiCallTimeRecord = APICallTimeRecord.start();
        Optional<PlaidItemDetail> plaidItemDetail = iPlaidItemService.getPlaidItemDetail(billingDetail.getAchPaymentInfo());
        apiCallTimeRecord.endApiCall();

        if(plaidItemDetail.isPresent()) {
            PlaidAPIError plaidAPIError = plaidItemDetail.get().getPlaidItem().getError();
            PlaidErrorTypeE plaidErrorTypeE = plaidAPIError.getErrorType();
            PlaidErrorCodeE plaidErrorCodeE = plaidAPIError.getErrorCode();


            if(plaidErrorTypeE == PlaidErrorTypeE.ITEM_ERROR) {
                if(plaidErrorCodeE == PlaidErrorCodeE.ITEM_LOGIN_REQUIRED) {
                    LOGGER.warn("Plaid Item Login Quality Gate:=> Failed. Detected that this user's current Plaid link to Bank account requires a new login");
                    executionResult.addValidationAdvice(ValidationAdvice.newInstance("Unable to validate your Bank Account Credentials."));

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
                    executionResult.addPlatformActionRequiredE(PlatformActionRequiredE.User_Plaid_Account_Login);
                }
            }

        }
    }

}

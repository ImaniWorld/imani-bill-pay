package com.imani.bill.pay.service.payment.stripe;

import com.imani.bill.pay.domain.execution.ExecutionError;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.payment.ACHPaymentInfo;
import com.imani.bill.pay.domain.payment.config.StripeAPIConfig;
import com.imani.bill.pay.domain.payment.stripe.AccountObjFieldsE;
import com.imani.bill.pay.domain.property.PropertyManager;
import com.imani.bill.pay.domain.property.PropertyOwner;
import com.imani.bill.pay.service.payment.ACHPaymentInfoService;
import com.imani.bill.pay.service.payment.IACHPaymentInfoService;
import com.imani.bill.pay.service.property.manager.IPropertyManagerService;
import com.imani.bill.pay.service.property.manager.PropertyManagerService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation to provide Stripe Account creation functionality.
 * Stripe API Account document:  https://stripe.com/docs/api/accounts/create
 *
 * @author manyce400
 */
@Service(StripeAccountService.SPRING_BEAN)
public class StripeAccountService implements IStripeAccountService {


    @Autowired
    private StripeAPIConfig stripeAPIConfig;

    @Autowired
    @Qualifier(ACHPaymentInfoService.SPRING_BEAN)
    private IACHPaymentInfoService iachPaymentInfoService;


    @Autowired
    @Qualifier(PropertyManagerService.SPRING_BEAN)
    private IPropertyManagerService iPropertyManagerService;


    public static final String SPRING_BEAN = "com.imani.bill.pay.service.payment.stripe.StripeAccountService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(StripeAccountService.class);


    @Transactional
    @Override
    public ExecutionResult createCustomStripeAccount(PropertyOwner propertyOwner) {
        Assert.notNull(propertyOwner, "propertyOwner cannot be null");
        ExecutionResult executionResult = new ExecutionResult();

        return executionResult;
    }

    @Transactional
    @Override
    public ExecutionResult createCustomStripeAccount(PropertyManager propertyManager) {
        Assert.notNull(propertyManager, "propertyManager cannot be null");
        LOGGER.info("Creating custom Stripe account for PropertyOwner on Property");

        Stripe.apiKey = stripeAPIConfig.getApiKey();

        ExecutionResult executionResult = new ExecutionResult();

        // This is a requirement in Imani BillPay as ACH payment account should have been already authorized through Plaid
        // Retrieve already created ACHPaymentInfo with Plaid details.
        ACHPaymentInfo achPaymentInfo = iachPaymentInfoService.findPrimaryPamentInfo(propertyManager);

        if (achPaymentInfo != null) {
            try {
                Account stripeAccount = getStripeAccount(propertyManager);
                propertyManager.setStripeAcctID(stripeAccount.getId());
                iPropertyManagerService.save(propertyManager);

                // Add bank account details
                addBankAcctDetails(stripeAccount, achPaymentInfo);
            } catch (StripeException e) {
                LOGGER.error("Failed to create new Stripe Connected Account", e);
                executionResult.addExecutionError(ExecutionError.of("Failed to create Stripe Account info"));
            }
        }

        return executionResult;
    }


    Account getStripeAccount(PropertyManager propertyManager) throws StripeException {
        LOGGER.debug("Attempting to create or retrieve connected Stripe Account for propertyManager");
        if(StringUtils.isEmpty(propertyManager.getStripeAcctID())) {
            Map<String, Object> params = AccountObjFieldsE.getAccountCreateParams(propertyManager);
            Account stripeAccount = Account.create(params);
            return stripeAccount;
        }

        // Get already created connected account
        return Account.retrieve(propertyManager.getStripeAcctID());
    }


    void addBankAcctDetails(Account stripeAccount, ACHPaymentInfo achPaymentInfo) throws StripeException {
        LOGGER.info("Adding created and linked Stripe Bank Account token from Plaid Acct details to connected account");
        Map<String, Object> params = new HashMap<>();
        params.put("external_account", achPaymentInfo.getStripeBankAcct().getBankAcctToken());

        // Add Plaid Account to PlaidBankAcct details
        BankAccount bankAccount = (BankAccount) stripeAccount.getExternalAccounts().create(params);
        iachPaymentInfoService.updateStripeBankAcct(bankAccount, achPaymentInfo);
        iachPaymentInfoService.saveACHPaymentInfo(achPaymentInfo);
    }


    public static void main(String[] args) {
        String acctID = "acct_1Fkzt3IaC95ufTL8";
        Stripe.apiKey = "sk_test_Yk6COIu8mI0GH5ttBzV6Ubu800txYLbF2A";// secret key
        try {
            System.out.println("Deleting Stripe Account with ID:=>  " + acctID);
            Account account = Account.retrieve(acctID);
            Account deletedAccount = account.delete();
            System.out.println("Stripe account has been successfully deleted....");
        } catch (StripeException e) {
            System.out.println("Failed to delete account");
            e.printStackTrace();
        }
    }


}
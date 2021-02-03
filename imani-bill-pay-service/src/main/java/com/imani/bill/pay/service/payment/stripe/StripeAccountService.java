package com.imani.bill.pay.service.payment.stripe;

import com.imani.bill.pay.domain.business.Business;
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

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
    public Optional<Account> createConnectedStripeAcct(Business business) {
        Assert.notNull(business, "business cannot be null");
        Assert.isNull(business.getStripeAcctID(), "Business already has a Stripe Acct ID");
        Optional<Account> account = createStripeAccount(business);
        return account;
    }

    @Override
    public Optional<Account> getConnectedStripeAcct(Business business) {
        Assert.notNull(business, "Business cannot be null");
        Assert.notNull(business.getStripeAcctID(), "Business has no connected Stripe Acct ID");

        LOGGER.info("Attempting to retrieve Connected Stripe Acct for Business[{}]", business.getName());

        try {
            Account account = Account.retrieve(business.getStripeAcctID());
            if(account != null) {
                return Optional.of(account);
            }
        } catch (StripeException e) {
            LOGGER.warn("Exception occurred while trying to retrieve a connected Stripe Acct", e);
        }

        return Optional.empty();
    }

    @Override
    public boolean removeConnectedStripeAcct(Business business) {
        Assert.notNull(business, "Business cannot be null");
        Assert.notNull(business.getStripeAcctID(), "Business has no connected Stripe Acct ID");

        Optional<Account> account = getConnectedStripeAcct(business);
        if(account.isPresent()) {
            try {
                account.get().delete();
                return true;
            } catch (StripeException e) {
                LOGGER.warn("Exception occurred while trying to delete a connected Stripe Acct", e);
            }
        }

        return false;
    }

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
                Account stripeAccount = null;//getStripeAccount(propertyManager);
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


    Optional<Account> createStripeAccount(Business business)  {
        LOGGER.debug("Attempting to create connected Stripe Account for Business[{}]", business.getName());

        try {
            Map<String, Object> params = AccountObjFieldsE.getAccountCreateParams(business);
            Account stripeAccount = Account.create(params);
            return Optional.of(stripeAccount);
        } catch (StripeException e) {
            LOGGER.warn("Exception occurred while trying to create a connected Stripe Acct", e);
            return Optional.empty();
        }
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
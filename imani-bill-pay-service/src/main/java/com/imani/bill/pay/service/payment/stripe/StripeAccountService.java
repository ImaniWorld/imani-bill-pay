package com.imani.bill.pay.service.payment.stripe;

import com.imani.bill.pay.domain.payment.ACHPaymentInfo;
import com.imani.bill.pay.domain.payment.plaid.PlaidBankAcct;
import com.imani.bill.pay.domain.payment.config.StripeAPIConfig;
import com.imani.bill.pay.domain.property.PropertyManager;
import com.imani.bill.pay.domain.property.PropertyOwner;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Arrays;
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

    public static final String CARD_PAYMENTS_CAPABILITY = "card_payments";

    public static final String TRANSFERS_CAPABILITY = "transfers";


    public static final String SPRING_BEAN = "com.imani.bill.pay.service.payment.stripe.StripeAccountService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(StripeAccountService.class);


    @Override
    public Optional<ACHPaymentInfo> createCustomStripeAccount(PropertyOwner propertyOwner, PlaidBankAcct plaidBankAcct) {
        Assert.notNull(propertyOwner, "propertyOwner cannot be null");
        Assert.notNull(plaidBankAcct, "plaidBankAcct cannot be null");
        return Optional.empty();
    }

    @Override
    public Optional<ACHPaymentInfo> createCustomStripeAccount(PropertyManager propertyManager, PlaidBankAcct plaidBankAcct) {
        Assert.notNull(propertyManager, "propertyManager cannot be null");
        Assert.notNull(plaidBankAcct, "plaidBankAcct cannot be null");
        LOGGER.info("Creating custom Stripe account for PropertyOwner on Property");

        Stripe.apiKey = stripeAPIConfig.getApiKey();

        Map<String, Object> params = new HashMap<>();
        params.put("country", "US");
        params.put("type", "custom");
        params.put("business_type", "company");
        params.put("email", propertyManager.getEmbeddedContactInfo().getEmail());
        params.put("requested_capabilities", Arrays.asList(CARD_PAYMENTS_CAPABILITY, TRANSFERS_CAPABILITY));

        try {
            Account stripeAccount = Account.create(params);
            ACHPaymentInfo achPaymentInfo = ACHPaymentInfo.builder()
                    .isVerified(false)
                    .build();

            // Add bank account details
            addBankAcctDetails(stripeAccount, plaidBankAcct);

            return Optional.of(achPaymentInfo);
        } catch (StripeException e) {
            LOGGER.error("Failed to create new Stripe Connected Account", e);
        }

        return Optional.empty();
    }

    @Override
    public boolean deleteCustomStripeAccount(ACHPaymentInfo achPaymentInfo) {
        Assert.notNull(achPaymentInfo, "achPaymentInfo cannot be null");
//        //LOGGER.info("Deleting custom Stripe Account with AcctID: {}", achPaymentInfo.getStripeAcctID());
//
//        Stripe.apiKey = stripeAPIConfig.getApiKey();
//
//        try {
//            Account stripeAccount = Account.retrieve(achPaymentInfo.getStripeAcctID());
//            Account deletedAccount = stripeAccount.delete();
//            return true;
//        } catch (StripeException e) {
//            LOGGER.info("Failed to delete Custom Stripe Acccount", e);
//        }

        return false;
    }

    boolean addBankAcctDetails(Account stripeAccount, PlaidBankAcct plaidBankAcct) {
        LOGGER.info("Adding Plaid PlaidBankAcct details to connected account");
        Map<String, Object> params = new HashMap<>();
        params.put("external_account", plaidBankAcct.getAccountID());

        try {
            // Add Plaid Account to PlaidBankAcct details
            BankAccount bankAccount = (BankAccount) stripeAccount.getExternalAccounts().create(params);
            return true;
        } catch (StripeException e) {
            LOGGER.warn("Failed to add PlaidBankAcct information to Stripe AcctID => {}", stripeAccount.getId());
        }

        return false;
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
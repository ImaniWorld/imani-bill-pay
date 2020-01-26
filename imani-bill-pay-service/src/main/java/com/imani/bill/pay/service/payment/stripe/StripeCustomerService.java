package com.imani.bill.pay.service.payment.stripe;

import com.imani.bill.pay.domain.payment.ACHPaymentInfo;
import com.imani.bill.pay.domain.payment.config.PlaidAPIConfig;
import com.imani.bill.pay.domain.payment.config.StripeAPIConfig;
import com.imani.bill.pay.domain.payment.plaid.*;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.repository.IUserRecordRepository;
import com.imani.bill.pay.service.payment.ACHPaymentInfoService;
import com.imani.bill.pay.service.payment.IACHPaymentInfoService;
import com.imani.bill.pay.service.payment.plaid.IPlaidAPIService;
import com.imani.bill.pay.service.payment.plaid.IPlaidAccountMasterService;
import com.imani.bill.pay.service.payment.plaid.PlaidAPIService;
import com.imani.bill.pay.service.payment.plaid.PlaidAccountMasterService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.BankAccount;
import com.stripe.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author manyce400
 */
@Service(StripeCustomerService.SPRING_BEAN)
public class StripeCustomerService implements IStripeCustomerService {


    @Autowired
    private StripeAPIConfig stripeAPIConfig;

    @Autowired
    private PlaidAPIConfig plaidAPIConfig;

    @Autowired
    private IUserRecordRepository iUserRecordRepository;

    @Autowired
    @Qualifier(PlaidAPIService.SPRING_BEAN)
    private IPlaidAPIService iPlaidAPIService;

    @Autowired
    @Qualifier(ACHPaymentInfoService.SPRING_BEAN)
    private IACHPaymentInfoService iachPaymentInfoService;

    @Autowired
    @Qualifier(PlaidAccountMasterService.SPRING_BEAN)
    private IPlaidAccountMasterService iPlaidAccountMasterService;


    private static final String EMAIL_CUSTOMER_PARAM = "email";

    private static final String BANK_ACCT_SOURCE_CUSTOMER_PARAM = "source";


    public static final String SPRING_BEAN = "com.imani.bill.pay.service.payment.stripe.StripeCustomerService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(StripeCustomerService.class);

    /**
     * Create and return a Stripe Customer
     * @param userRecord
     * @return
     */
    @Override
    @Transactional
    public Optional<Customer> createStripeCustomer(UserRecord userRecord) {
        Assert.notNull(userRecord, "UserRecord cannot be null");
        //Assert.isTrue(userRecord.getUserRecordTypeE().isEndUser(), "Stripe Customer can only be created for End Users only.");
        Assert.notNull(userRecord.getEmbeddedContactInfo(), "EmbeddedContactInfo cannot be null");
        LOGGER.info("Creating Stripe Customer object reference for user:=> {}", userRecord.getEmbeddedContactInfo().getEmail());

        Stripe.apiKey = stripeAPIConfig.getApiKey();

        Map<String, Object> params = new HashMap<>();
        params.put(EMAIL_CUSTOMER_PARAM, userRecord.getEmbeddedContactInfo().getEmail());

        try {
            Customer customer = Customer.create(params);
            LOGGER.info("Successfully Created Stripe Customer, updating UserRecord with Id:=> {}", customer.getId());
            userRecord.setStripeCustomerID(customer.getId());
            iUserRecordRepository.save(userRecord);
            return Optional.of(customer);
        } catch (StripeException e) {
            LOGGER.warn("Failed to create Stripe Customer object reference, user will not be able to make payments", e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Customer> retrieveStripeCustomer(UserRecord userRecord) {
        Assert.notNull(userRecord, "UserRecord cannot be null");
        //Assert.isTrue(userRecord.getUserRecordTypeE().isEndUser(), "Stripe Customer can only be created for End Users only.");

        Stripe.apiKey = stripeAPIConfig.getApiKey();

        LOGGER.info("Attempting to retrieve existing Stripe Customer object for for user:=> {}", userRecord.getEmbeddedContactInfo().getEmail());

        try {
            Customer stripeCustomer = Customer.retrieve(userRecord.getStripeCustomerID());
            return Optional.of(stripeCustomer);
        } catch (StripeException e) {
            LOGGER.warn("Failed to retrieve Stripe Customer for user:=> {}", userRecord.getEmbeddedContactInfo().getEmail());
        }

        return Optional.empty();
    }


    @Override
    public boolean deleteStripeCustomer(UserRecord userRecord) {
        Assert.notNull(userRecord, "UserRecord cannot be null");

        Stripe.apiKey = stripeAPIConfig.getApiKey();

        LOGGER.info("Attempting to delete existing Stripe Customer object for for user:=> {}", userRecord.getEmbeddedContactInfo().getEmail());

        // First retrieve the Stripe Customer object for this UserRecord
        Optional<Customer> stripeCustomer = retrieveStripeCustomer(userRecord);

        if(stripeCustomer.isPresent()) {
            try {
                stripeCustomer.get().delete();
                return true;
            } catch (StripeException e) {
                LOGGER.warn("Exception occurred while trying to delete existing Stripe Customer", e);
            }
        }

        return false;
    }

    @Override
    public Optional<ACHPaymentInfo> createPlaidStripeCustomerBankAcct(UserRecord userRecord, String plaidPublicToken, String plaidAccountID) {
        Assert.notNull(userRecord, "UserRecord cannot be null");
        Assert.notNull(userRecord.getEmbeddedContactInfo(), "EmbeddedContactInfo cannot be null");
        Assert.notNull(plaidPublicToken, "achPaymentInfo cannot be null");
        Assert.notNull(plaidAccountID, "plaidAccountID cannot be null");
        //Assert.isTrue(userRecord.getUserRecordTypeE().isEndUser(), "Stripe Customer can only be created for End Users only.");

        // Fetch UserRecord from persistent store
        userRecord = iUserRecordRepository.findByUserEmail(userRecord.getEmbeddedContactInfo().getEmail());

        Optional<Customer> stripeCustomer;

        if (userRecord.getStripeCustomerID() == null) {
            stripeCustomer = createStripeCustomer(userRecord);
        } else {
            stripeCustomer = retrieveStripeCustomer(userRecord);
        }

        if(stripeCustomer.isPresent()) {
            LOGGER.info("Creating Stripe Customer BankAcct for user:=> {}", userRecord.getEmbeddedContactInfo().getEmail());

            // In order to execute Plaid API request for this Plaid account, we will need an Access Token using passed Public Token.
            Optional<PlaidAccessTokenResponse> plaidAccessTokenResponse = iPlaidAPIService.exchangePublicTokenForAccess(plaidPublicToken, userRecord);


            Stripe.apiKey = stripeAPIConfig.getApiKey();

            if (plaidAccessTokenResponse.isPresent()) {
                // We can now attempt to create a Stripe BankAcct that links to selected plaidAccountID
                Optional<StripeBankAccountResponse> stripeBankAccountResponse = iPlaidAccountMasterService.createStripeAccount(plaidAccessTokenResponse.get(), plaidAccountID);

                // Get the details of the linked Plaid Account and update the ACH payment info.
                PlaidAPIRequest plaidAPIRequest = buildPlaidAPIRequestForItemBankAccounts(plaidAccessTokenResponse.get().getAccessToken());
                Optional<PlaidItemAccountsResponse> plaidItemAccountsResponse = null;//iPlaidAPIService.getPlaidItemAccounts(plaidAPIRequest);

                if(stripeBankAccountResponse.isPresent()
                        && plaidItemAccountsResponse.isPresent()
                        && StringUtils.isEmpty(stripeBankAccountResponse.get().getErrorMessage())) {
                    LOGGER.info("Successfully retrieved a created StripeBank Token associated with created Plaid Linked AcctID, creating Customer BankAcct...");
                    Map<String, Object> params = new HashMap<>();
                    params.put(BANK_ACCT_SOURCE_CUSTOMER_PARAM, stripeBankAccountResponse.get().getStripeBankAcctToken());

                    try {
                        ACHPaymentInfo achPaymentInfo = iachPaymentInfoService.buildPrimaryACHPaymentInfo(userRecord);
                        BankAccount bankAccount = (BankAccount) stripeCustomer.get().getSources().create(params);

                        // Update Stripe account details on ACHPaymentInfo
                        iachPaymentInfoService.updateStripeBankAcct(bankAccount, achPaymentInfo);

                        // Update Plaid Bank account details on ACHPaymentInfo
                        PlaidBankAcct plaidBankAcct = plaidItemAccountsResponse.get().getFirst();
                        plaidBankAcct.setPlaidAccessToken(plaidAccessTokenResponse.get().getAccessToken());
                        iachPaymentInfoService.updatePlaidBankAcct(plaidBankAcct, achPaymentInfo);

                        return Optional.of(achPaymentInfo);
                    } catch (StripeException e) {
                        LOGGER.warn("Failed to create Stripe BankAcct for UserRecord on Linked Plaid Account", e);
                    }
                }
            }
        }

        return Optional.empty();
    }


    PlaidAPIRequest buildPlaidAPIRequestForItemBankAccounts(String accessToken) {
        PlaidAPIRequest plaidAPIRequest = PlaidAPIRequest.builder()
                .secret(plaidAPIConfig.getSecret())
                .clientID(plaidAPIConfig.getClientID())
                .accessToken(accessToken)
                .build();
        return plaidAPIRequest;
    }
}

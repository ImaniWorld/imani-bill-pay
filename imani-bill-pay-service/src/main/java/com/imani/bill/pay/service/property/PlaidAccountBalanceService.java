package com.imani.bill.pay.service.property;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imani.bill.pay.domain.payment.*;
import com.imani.bill.pay.domain.payment.repository.IACHPaymentInfoRepository;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.service.rest.RestTemplateConfigurator;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

/**
 * @author manyce400
 */
@Service(PlaidAccountBalanceService.SPRING_BEAN)
public class PlaidAccountBalanceService implements IPlaidAccountBalanceService {



    @Autowired
    private ObjectMapper mapper;

    @Autowired
    @Qualifier(RestTemplateConfigurator.SERVICE_REST_TEMPLATE)
    private RestTemplate restTemplate;

    @Autowired
    private IACHPaymentInfoRepository iachPaymentInfoRepository;

    @Autowired
    private IPlaidAPIExecMetricBuilderService iPlaidAPIExecMetricBuilderService;


    public static final String SPRING_BEAN = "com.imani.cash.domain.service.payment.ach.plaid.PlaidAccountBalanceService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PlaidAccountBalanceService.class);


    @Override
    public Optional<Balance> getACHPaymentInfoBalances(UserRecord userRecord) {
        Assert.notNull(userRecord, "UserRecord cannot be null");
        LOGGER.info("Finding ACHPaymentInfo for user:=> {}", userRecord.getEmbeddedContactInfo().getEmail());
        ACHPaymentInfo achPaymentInfo = iachPaymentInfoRepository.findUserACHPaymentInfo(userRecord);
        return getACHPaymentInfoBalances(achPaymentInfo);
    }

    @Override
    public Optional<Balance> getACHPaymentInfoBalances(ACHPaymentInfo achPaymentInfo) {
        Assert.notNull(achPaymentInfo, "ACHPaymentInfo cannot be null");

        LOGGER.info("Executing Plaid request to check balance for acctID:=>  {}", achPaymentInfo.getPlaidAcctID());

        ACHInfoRequestObj achInfoRequestObj = ACHInfoRequestObj.builder()
                .clientID("5cf09bb96590ed001352c26f")
                .secret("e0cd2b5363f8864089a2cf23d2e1de")
                .accessToken("access-sandbox-41e7c7bf-4eee-41cb-bfea-b4c2ec227a7b")
                .accountID(achPaymentInfo.getPlaidAcctID())
                //.accountID("m1ELdBMPLAHBGR7eDGaktknLRNaN8VHLe663j")
                .build();

        // Build HTTP Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        DateTime apiInvocationStartDate = null;
        DateTime apiInvocationEndDate = null;

        try {
            String jsonRequest = mapper.writeValueAsString(achInfoRequestObj);
            HttpEntity<String> request = new HttpEntity<>(jsonRequest, headers);

            apiInvocationStartDate = DateTime.now();
            BankAccountList bankAccountList = restTemplate.postForObject("https://sandbox.plaid.com/accounts/balance/get", request, BankAccountList.class);
            apiInvocationEndDate = DateTime.now();

            // Capture and record metrics for succesful API call.
            iPlaidAPIExecMetricBuilderService.buildBalancePlaidAPIExecMetricOnSuccess(achPaymentInfo.getUserRecord(), apiInvocationStartDate, apiInvocationEndDate);

            // We expect only 1 bank account back in the list returned always return first in the list
            BankAccount bankAccount = bankAccountList.getAccounts().get(0);
            return Optional.of(bankAccount.getBalances());
        } catch (Exception e) {
            // Capture and record metrics for succesful API call.
            LOGGER.error("Failed to execute Plaid API balance call", e);
            iPlaidAPIExecMetricBuilderService.buildBalancePlaidAPIExecMetricOnFailure(achPaymentInfo.getUserRecord(), e.getMessage(), apiInvocationStartDate, apiInvocationEndDate);
        }
        
        return Optional.empty();
    }


    @Override
    public boolean availableBalanceCoversPayment(UserRecord userRecord, Double paymentAmnt) {
        Assert.notNull(userRecord, "UserRecord cannot be null");
        Assert.notNull(paymentAmnt, "paymentAmnt cannot be null");
        LOGGER.info("Finding ACHPaymentInfo for user:=> {}", userRecord.getEmbeddedContactInfo().getEmail());
        ACHPaymentInfo achPaymentInfo = iachPaymentInfoRepository.findUserACHPaymentInfo(userRecord);
        return availableBalanceCoversPayment(achPaymentInfo, paymentAmnt);
    }

    @Override
    public boolean availableBalanceCoversPayment(ACHPaymentInfo achPaymentInfo, Double paymentAmnt) {
        Assert.notNull(achPaymentInfo, "ACHPaymentInfo cannot be null");
        Assert.notNull(paymentAmnt, "paymentAmnt cannot be null");
        Assert.isTrue(paymentAmnt.doubleValue() > 0,  "");

        LOGGER.info("Executing Plaid request to check available balance on acctID:=>  {} for payment: {}", achPaymentInfo.getPlaidAcctID(), paymentAmnt);

        Optional<Balance> balance = getACHPaymentInfoBalances(achPaymentInfo);
        if(balance.isPresent()) {
            LOGGER.info("Current available balance on account is:=> {}", balance.get().getAvailable());
            return balance.get().getAvailable().doubleValue() >= paymentAmnt;
        } else {
            LOGGER.warn("Could not find available balance information on account, allowing payment....");
            return true;
        }
    }

}

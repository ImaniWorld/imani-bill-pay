package com.imani.bill.pay.service.payment.plaid;

import com.google.common.collect.ImmutableSet;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.payment.ACHPaymentInfo;
import com.imani.bill.pay.domain.payment.config.PlaidAPIConfig;
import com.imani.bill.pay.domain.payment.plaid.*;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.repository.IUserRecordRepository;
import com.imani.bill.pay.service.mock.IMockACHPaymentInfoTestBuilder;
import com.imani.bill.pay.service.mock.IMockUserRecordTestBuilder;
import com.imani.bill.pay.service.payment.IACHPaymentInfoService;
import com.imani.bill.pay.service.payment.security.PlaidAccessTokenSecurityAdviseService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

/**
 * @author manyce400
 */
@RunWith(MockitoJUnitRunner.class)
public class PlaidAccountMasterServiceTest implements IMockUserRecordTestBuilder, IMockACHPaymentInfoTestBuilder {


    private UserRecord userRecord;

    private ACHPaymentInfo achPaymentInfo;

    @Mock
    private PlaidAPIConfig plaidAPIConfig;
    
    @Mock
    private IPlaidAPIService iPlaidAPIService;

    @Mock
    private IUserRecordRepository iUserRecordRepository;

    @Mock
    private IACHPaymentInfoService iachPaymentInfoService;

    @Mock
    private PlaidAccessTokenSecurityAdviseService plaidAccessTokenSecurityAdviseService;

    @InjectMocks
    private PlaidAccountMasterService plaidAccountMasterService;


    @Before
    public void beforeTest() {
        // Mock out all calls to return default objects
        userRecord = buildUserRecord();
        achPaymentInfo = buildACHPaymentInfoPlaid(userRecord, "PLAID-908999");
        Mockito.when(iUserRecordRepository.findByUserEmail(Mockito.anyString())).thenReturn(userRecord);
        Mockito.when(iachPaymentInfoService.findPrimaryPamentInfo(userRecord)).thenReturn(achPaymentInfo);
    }
    
    //@Test
    public void testBuildPlaidAPIRequestForStripeAccountCreate() {
        PlaidAPIRequest plaidAPIRequest = plaidAccountMasterService.buildPlaidAPIRequestForStripeAccountCreate(achPaymentInfo);
        Assert.assertEquals("SYS-RANDOM-89000", plaidAPIRequest.getAccessToken());
        Assert.assertEquals("PLAID-908999", plaidAPIRequest.getAccountID());
    }


    @Test
    public void testCreateStripeAcctForPrimaryPlaidAcct() {
        // Mock out call to create Stripe Bank account from Plaid
        StripeBankAccountResponse stripeBankAccountResponse = StripeBankAccountResponse.builder()
                .stripeBankAcctToken("bktok_MOCK_TEST_ACCT")
                .build();
        Mockito.when(iPlaidAPIService.createStripeBankAccount(Mockito.any(), Mockito.any())).thenReturn(Optional.of(stripeBankAccountResponse));

        // Execute call and verify results
        ExecutionResult executionResult = plaidAccountMasterService.createStripeAcctForPrimaryPlaidAcct(userRecord);
        Assert.assertEquals("bktok_MOCK_TEST_ACCT", achPaymentInfo.getStripeBankAcct().getBankAcctToken());
        Assert.assertTrue(executionResult.isExecutionSuccessful());
        Assert.assertEquals(0, executionResult.getValidationAdvices().size());
    }

    @Test
    public void testLinkPlaidBankAcctUserRecord() {
        PlaidAccessTokenResponse plaidAccessTokenResponse = PlaidAccessTokenResponse.builder()
                .accessToken("access-development-457777940YY")
                .build();
        Mockito.when(iPlaidAPIService.exchangePublicTokenForAccess(Mockito.any(), Mockito.any())).thenReturn(Optional.of(plaidAccessTokenResponse));

        Mockito.when(plaidAccessTokenSecurityAdviseService.getAdvice(Mockito.any())).thenReturn(ImmutableSet.of());

        PlaidBankAcct plaidBankAcct = PlaidBankAcct.builder()
                .accountID("Txyxi08480w9md")
                .name("Dummy Test Acct")
                .officialName("Official Test Acct")
                .build();
        PlaidItemAccountsResponse plaidItemAccountsResponse = PlaidItemAccountsResponse.builder()
                .plaidItemInfo(new PlaidItem())
                .plaidBankAcct(plaidBankAcct)
                .build();
        Mockito.when(iPlaidAPIService.getPlaidItemAccounts(Mockito.any(), Mockito.any())).thenReturn(Optional.of(plaidItemAccountsResponse));

        ExecutionResult executionResult = plaidAccountMasterService.linkPlaidBankAcct("beta_env_337737", userRecord);
        System.out.println("executionResult = " + executionResult);
    }

}
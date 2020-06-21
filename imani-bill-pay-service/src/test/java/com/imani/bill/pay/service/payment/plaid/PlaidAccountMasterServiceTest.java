package com.imani.bill.pay.service.payment.plaid;

import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.payment.ACHPaymentInfo;
import com.imani.bill.pay.domain.payment.config.PlaidAPIConfig;
import com.imani.bill.pay.domain.payment.plaid.PlaidAPIRequest;
import com.imani.bill.pay.domain.payment.plaid.StripeBankAccountResponse;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.repository.IUserRecordRepository;
import com.imani.bill.pay.service.mock.IMockACHPaymentInfoTestBuilder;
import com.imani.bill.pay.service.mock.IMockUserRecordTestBuilder;
import com.imani.bill.pay.service.payment.IACHPaymentInfoService;
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

}
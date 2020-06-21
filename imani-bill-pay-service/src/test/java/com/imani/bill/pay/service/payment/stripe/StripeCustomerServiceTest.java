package com.imani.bill.pay.service.payment.stripe;

import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.payment.ACHPaymentInfo;
import com.imani.bill.pay.domain.payment.config.StripeAPIConfig;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.repository.IUserRecordRepository;
import com.imani.bill.pay.service.mock.IMockACHPaymentInfoTestBuilder;
import com.imani.bill.pay.service.mock.IMockUserRecordTestBuilder;
import com.imani.bill.pay.service.payment.IACHPaymentInfoService;
import com.stripe.model.BankAccount;
import com.stripe.model.Customer;
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
public class StripeCustomerServiceTest implements IMockUserRecordTestBuilder, IMockACHPaymentInfoTestBuilder {
    
    
    private UserRecord userRecord;
    
    private ACHPaymentInfo achPaymentInfo;

    @Mock
    private StripeAPIConfig stripeAPIConfig;

    @Mock
    private IStripeCustomerAPIFacade iStripeCustomerAPIFacade;
    
    @Mock
    private IUserRecordRepository iUserRecordRepository;
    
    @Mock
    private IACHPaymentInfoService iachPaymentInfoService;
    
    @InjectMocks
    private StripeCustomerService stripeCustomerService;
    
    @Before
    public void beforeTest() {
        // Mock out all calls to return default objects
        userRecord = buildUserRecord();
        achPaymentInfo = buildACHPaymentInfo(userRecord, "btok_3748SYDNEY4EVA");
        Mockito.when(iUserRecordRepository.findByUserEmail(Mockito.anyString())).thenReturn(userRecord);
        Mockito.when(iachPaymentInfoService.findPrimaryPamentInfo(userRecord)).thenReturn(achPaymentInfo);
    }
    
    
    @Test
    public void testCreatePlaidStripeCustomerBankAcct() {
        // Create a mock customer
        Customer customer = new Customer();
        customer.setId("xycd-1234");
        Mockito.when(iStripeCustomerAPIFacade.createStripeCustomer(userRecord)).thenReturn(Optional.of(customer));

        // Build mock bank account
        BankAccount bankAccount = buildMockBankAccount();
        Mockito.when(iStripeCustomerAPIFacade.createBankAccount(Mockito.any(), Mockito.anyMap())).thenReturn(Optional.of(bankAccount));

        // Execute and verify results
        ExecutionResult executionResult = stripeCustomerService.createPlaidStripeCustomerBankAcct(userRecord);
        Assert.assertEquals("xycd-1234", userRecord.getStripeCustomerID());
        Assert.assertTrue(executionResult.isExecutionSuccessful());
        Assert.assertEquals(0, executionResult.getValidationAdvices().size());
        System.out.println("executionResult = " + executionResult);
    }


    private BankAccount buildMockBankAccount() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId("Bank_Numero_Uno");
        bankAccount.setCountry("USA");
        bankAccount.setCurrency("USD");
        return bankAccount;
    }
}

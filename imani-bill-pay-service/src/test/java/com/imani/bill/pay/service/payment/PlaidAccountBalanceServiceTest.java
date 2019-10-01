package com.imani.bill.pay.service.payment;

import com.imani.bill.pay.domain.payment.repository.IACHPaymentInfoRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * TODO complete test implementation
 * @author manyce400
 */
@RunWith(MockitoJUnitRunner.class)
public class PlaidAccountBalanceServiceTest {


    @Mock
    private IACHPaymentInfoRepository iachPaymentInfoRepository;

    @Mock
    private IPlaidAPIStatisticBuilderService iPlaidAPIStatisticBuilderService;

    @InjectMocks
    private PlaidAccountBalanceService plaidAccountBalanceService;


    @Test
    public void testAvailableBalanceCoversPayment() {

    }
}

package com.imani.bill.pay.service.payment.plaid;

import com.imani.bill.pay.domain.payment.plaid.PlaidAPIInvocationStatistic;
import com.imani.bill.pay.domain.payment.plaid.repository.IPlaidAPIInvocationStatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author manyce400
 */
@Service(PlaidAPIInvocationStatisticService.SPRING_BEAN)
public class PlaidAPIInvocationStatisticService implements IPlaidAPIInvocationStatisticService {


    @Autowired
    private IPlaidAPIInvocationStatisticRepository iPlaidAPIInvocationStatisticRepository;

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.payment.plaid.PlaidAPIInvocationStatisticService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PlaidAPIInvocationStatisticService.class);


    @Override
    public void save(PlaidAPIInvocationStatistic plaidAPIInvocationStatistic) {
        Assert.notNull(plaidAPIInvocationStatistic, "PlaidAPIInvocationStatistic cannot be null");
        LOGGER.info("Saving plaidAPIInvocationStatistic:=> {}", plaidAPIInvocationStatistic);
        iPlaidAPIInvocationStatisticRepository.save(plaidAPIInvocationStatistic);
    }

}

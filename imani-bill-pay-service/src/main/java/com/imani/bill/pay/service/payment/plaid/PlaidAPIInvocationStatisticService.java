package com.imani.bill.pay.service.payment.plaid;

import com.imani.bill.pay.domain.payment.plaid.PlaidAPIInvocationE;
import com.imani.bill.pay.domain.payment.plaid.PlaidAPIInvocationStatistic;
import com.imani.bill.pay.domain.payment.plaid.PlaidAPIRequest;
import com.imani.bill.pay.domain.payment.plaid.PlaidAPIResponse;
import com.imani.bill.pay.domain.payment.plaid.repository.IPlaidAPIInvocationStatisticRepository;
import com.imani.bill.pay.domain.user.UserRecord;
import org.joda.time.DateTime;
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
        LOGGER.debug("Saving plaidAPIInvocationStatistic:=> {}", plaidAPIInvocationStatistic);
        iPlaidAPIInvocationStatisticRepository.save(plaidAPIInvocationStatistic);
    }

    @Override
    public PlaidAPIInvocationStatistic startPlaidAPIInvocation(UserRecord userRecord, PlaidAPIInvocationE plaidAPIInvocationE, PlaidAPIRequest plaidAPIRequest) {
        Assert.notNull(plaidAPIRequest, "PlaidAPIRequest cannot be null");
        LOGGER.info("Recording start of Plaid API invocation..");
        PlaidAPIInvocationStatistic plaidAPIInvocationStatistic = PlaidAPIInvocationStatistic.builder()
                .userRecord(userRecord)
                .plaidAPIInvocationE(plaidAPIInvocationE)
                .plaidAPIRequest(plaidAPIRequest)
                .apiInvocationStartDate(DateTime.now())
                .build();
        return plaidAPIInvocationStatistic;
    }

    @Override
    public <O extends PlaidAPIResponse> void endPlaidAPIInvocation(PlaidAPIInvocationStatistic plaidAPIInvocationStatistic, O plaidAPIResponse) {
        Assert.notNull(plaidAPIInvocationStatistic, "PlaidAPIInvocationStatistic cannot be null");
        plaidAPIInvocationStatistic.setApiInvocationEndDate(DateTime.now());
        plaidAPIInvocationStatistic.setPlaidAPIResponse(plaidAPIResponse);
        save(plaidAPIInvocationStatistic);
    }
}

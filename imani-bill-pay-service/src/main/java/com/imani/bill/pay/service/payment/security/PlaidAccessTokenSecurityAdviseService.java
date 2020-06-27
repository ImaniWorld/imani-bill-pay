package com.imani.bill.pay.service.payment.security;

import com.imani.bill.pay.domain.execution.ValidationAdvice;
import com.imani.bill.pay.domain.payment.IHasPaymentInfo;
import com.imani.bill.pay.domain.payment.plaid.PlaidAPIInvocationStatistic;
import com.imani.bill.pay.service.payment.plaid.IPlaidAPIInvocationStatisticService;
import com.imani.bill.pay.service.payment.plaid.PlaidAPIInvocationStatisticService;
import com.imani.bill.pay.service.validation.IValidationAdviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

/**
 * @author manyce400
 */
@Service(PlaidAccessTokenSecurityAdviseService.SPRING_BEAN)
public class PlaidAccessTokenSecurityAdviseService implements IValidationAdviceService<IHasPaymentInfo> {


    @Autowired
    @Qualifier(PlaidAPIInvocationStatisticService.SPRING_BEAN)
    private IPlaidAPIInvocationStatisticService iPlaidAPIInvocationStatisticService;


    public static final String SPRING_BEAN = "com.imani.bill.pay.service.payment.security.PlaidAccessTokenSecurityAdviseService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PlaidAccessTokenSecurityAdviseService.class);


    @Override
    public Set<ValidationAdvice> getAdvice(IHasPaymentInfo iHasPaymentInfo) {
        Assert.notNull(iHasPaymentInfo, "IHasPaymentInfo cannot be nulll");

        // Find all the failed attempts for this user to exchange public token
        List<PlaidAPIInvocationStatistic> failedAccessTokenAttempts = iPlaidAPIInvocationStatisticService.findFailedAccessTokenRequestCurrentDay(iHasPaymentInfo);

        if(!CollectionUtils.isEmpty(failedAccessTokenAttempts) && failedAccessTokenAttempts.size() >= 3) {
            LOGGER.warn("Plaid Access Token retrieval blocked.  Exceeded allowed tries to link a Plaid Bank Acct. Reset by Admin required to proceed.");
            return ValidationAdvice.newResultSet("Exceeded allowed tries to link a Plaid Bank Acct.");
        }

        return ValidationAdvice.newEmptyResultSet();
    }

}
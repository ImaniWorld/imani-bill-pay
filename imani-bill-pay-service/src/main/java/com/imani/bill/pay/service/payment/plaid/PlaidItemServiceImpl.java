package com.imani.bill.pay.service.payment.plaid;

import com.imani.bill.pay.domain.payment.ACHPaymentInfo;
import com.imani.bill.pay.domain.payment.config.PlaidAPIConfig;
import com.imani.bill.pay.domain.payment.plaid.PlaidAPIRequest;
import com.imani.bill.pay.domain.payment.plaid.PlaidItemDetail;
import com.imani.bill.pay.service.rest.RestTemplateConfigurator;
import com.imani.bill.pay.service.util.IRestUtil;
import com.imani.bill.pay.service.util.RestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

/**
 * Default implementation of IPlaidItemService
 *
 * @author manyce400
 */
@Service(PlaidItemServiceImpl.SPRING_BEAN)
public class PlaidItemServiceImpl implements IPlaidItemService {



    @Autowired
    private PlaidAPIConfig plaidAPIConfig;

    @Autowired
    @Qualifier(RestUtil.SPRING_BEAN)
    private IRestUtil iRestUtil;

    @Autowired
    @Qualifier(RestTemplateConfigurator.SERVICE_REST_TEMPLATE)
    private RestTemplate restTemplate;

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.payment.plaid.PlaidItemServiceImpl";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PlaidItemServiceImpl.class);


    @Override
    public Optional<PlaidItemDetail> getPlaidItemDetail(ACHPaymentInfo achPaymentInfo) {
        Assert.notNull(achPaymentInfo, "ACHPaymentInfo cannot be null");
        LOGGER.info("Executing Plaid request to get item info on AchPaymentInfo for user:=>  {}", achPaymentInfo.getUserRecord().getEmbeddedContactInfo().getEmail());

        PlaidAPIRequest achInfoRequestObj = PlaidAPIRequest.builder()
                .clientID(plaidAPIConfig.getClientID())
                .secret(plaidAPIConfig.getSecret())
                .accessToken(achPaymentInfo.getPlaidBankAcct().getPlaidAccessToken())
                .build();

        try {
            HttpEntity<String> request = iRestUtil.getObjectAsRequest(achInfoRequestObj);
            PlaidItemDetail plaidItemApiInfoResponse =  restTemplate.postForObject(plaidAPIConfig.getAPIPathURL("/item/get"), request, PlaidItemDetail.class);
            LOGGER.debug("Fetched PlaidItemDetail: {}", plaidItemApiInfoResponse);
            return Optional.of(plaidItemApiInfoResponse);
        } catch (Exception e) {
            LOGGER.error("Failed to retrieve PlaidItemDetail", e);
        }

        return Optional.empty();
    }

}
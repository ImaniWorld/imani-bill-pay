package com.imani.bill.pay.service.payment.stripe;

import com.imani.bill.pay.domain.payment.config.StripeAPIConfig;
import com.imani.bill.pay.domain.payment.repository.IACHPaymentInfoRepository;
import com.imani.bill.pay.domain.user.UserRecord;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author manyce400
 */
@Service(StripeChargeService.SPRING_BEAN)
public class StripeChargeService implements IStripeChargeService {


    @Autowired
    private StripeAPIConfig stripeAPIConfig;

    @Autowired
    private IACHPaymentInfoRepository iachPaymentInfoRepository;

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.payment.stripe.StripeChargeService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(StripeChargeService.class);

    @Override
    public Optional<Charge> createCustomerACHCharge(UserRecord userRecord, Double amountToCharge) {
        Assert.notNull(userRecord, "UserRecord cannot be null");
        Assert.notNull(amountToCharge, "amountToCharge cannot be null");

        LOGGER.info("Creating Stripe ACH charge for user :=> {} for amount:=> {}", userRecord.getEmbeddedContactInfo().getEmail(), amountToCharge);

        Stripe.apiKey = stripeAPIConfig.getApiKey();

        if (StringUtils.isEmpty(userRecord.getStripeCustomerID())) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("amount", amountToCharge);
            params.put("currency", "usd");
            params.put("customer", userRecord.getStripeCustomerID());

            try {
                Charge charge = Charge.create(params);
                //charge.getStatus()
                return Optional.of(charge);
            } catch (StripeException e) {
                LOGGER.warn("Failed to create UserRecord Customer Stripe charge", e);
            }
        }

        return Optional.empty();
    }
}

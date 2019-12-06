package com.imani.bill.pay.service.payment.stripe;

import com.imani.bill.pay.domain.user.UserRecord;
import com.stripe.model.Charge;

import java.util.Optional;

/**
 * @author manyce400
 */
public interface IStripeChargeService {

    public Optional<Charge> createCustomerACHCharge(UserRecord userRecord, Double amountToCharge);
}

package com.imani.bill.pay.service.payment.plaid;

import com.imani.bill.pay.domain.payment.ACHPaymentInfo;
import com.imani.bill.pay.domain.payment.plaid.PlaidItemDetail;

import java.util.Optional;

/**
 * @author manyce400
 */
public interface IPlaidItemService {

    public Optional<PlaidItemDetail> getPlaidItemDetail(ACHPaymentInfo achPaymentInfo);

}

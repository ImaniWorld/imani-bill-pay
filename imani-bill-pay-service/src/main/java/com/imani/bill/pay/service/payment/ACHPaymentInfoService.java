package com.imani.bill.pay.service.payment;

import com.imani.bill.pay.domain.payment.ACHPaymentInfo;
import com.imani.bill.pay.domain.payment.plaid.PlaidBankAcct;
import com.imani.bill.pay.domain.payment.repository.IACHPaymentInfoRepository;
import com.imani.bill.pay.domain.payment.stripe.StripeAcctHolderTypeE;
import com.imani.bill.pay.domain.payment.stripe.StripeBankAcct;
import com.imani.bill.pay.domain.payment.stripe.StripeBankAcctStatusE;
import com.imani.bill.pay.domain.user.UserRecord;
import com.stripe.model.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author manyce400
 */
@Service(ACHPaymentInfoService.SPRING_BEAN)
public class ACHPaymentInfoService implements IACHPaymentInfoService {


    @Autowired
    private IACHPaymentInfoRepository iachPaymentInfoRepository;

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.payment.ACHPaymentInfoService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ACHPaymentInfoService.class);


    @Override
    public ACHPaymentInfo buildPrimaryACHPaymentInfo(UserRecord userRecord) {
        Assert.notNull(userRecord, "userRecord cannot be null");
        ACHPaymentInfo achPaymentInfo = ACHPaymentInfo.builder()
                .userRecord(userRecord)
                .isPrimary(true)
                .build();
        return achPaymentInfo;
    }

    @Override
    public void updateStripeBankAcct(BankAccount stripeBankAccount, ACHPaymentInfo achPaymentInfo) {
        Assert.notNull(stripeBankAccount, "stripeBankAccount cannot be null");
        Assert.notNull(achPaymentInfo, "achPaymentInfo cannot be null");

        LOGGER.debug("Updating Stripe BankAcct details on ACHPaymentInfo with ID:=> {}", achPaymentInfo.getId());

        StripeBankAcct stripeBankAcct = StripeBankAcct.builder()
                .id(stripeBankAccount.getId())
                .object(stripeBankAccount.getObject())
                .accountHolderName(stripeBankAccount.getAccountHolderName())
                .accountHolderType(StripeAcctHolderTypeE.getByHolderType(stripeBankAccount.getAccountHolderType()))
                .bankName(stripeBankAccount.getBankName())
                .country(stripeBankAccount.getCountry())
                .currency(stripeBankAccount.getCurrency())
                .last4(stripeBankAccount.getLast4())
                .routingNumber(stripeBankAccount.getRoutingNumber())
                .acctStatus(StripeBankAcctStatusE.getByStatus(stripeBankAccount.getStatus()))
                .build();

        achPaymentInfo.setStripeBankAcct(stripeBankAcct);
    }

    @Override
    public void updatePlaidBankAcct(PlaidBankAcct plaidBankAcct, ACHPaymentInfo achPaymentInfo) {
        Assert.notNull(plaidBankAcct, "stripeBankAccount cannot be null");
        Assert.notNull(achPaymentInfo, "achPaymentInfo cannot be null");

        LOGGER.debug("Updating Plaid BankAcct details on ACHPaymentInfo with ID:=> {}", achPaymentInfo.getId());

        achPaymentInfo.setPlaidBankAcct(plaidBankAcct);
    }

    @Override
    public void saveACHPaymentInfo(ACHPaymentInfo achPaymentInfo) {
        Assert.notNull(achPaymentInfo, "achPaymentInfo cannot be null");
        LOGGER.debug("Saving achPaymentInfo:=> {}", achPaymentInfo);
        iachPaymentInfoRepository.save(achPaymentInfo);
    }

}
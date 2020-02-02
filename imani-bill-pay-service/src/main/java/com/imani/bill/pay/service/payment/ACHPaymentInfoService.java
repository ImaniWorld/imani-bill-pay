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

import java.util.Optional;

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
    public ACHPaymentInfo findUserPrimaryPamentInfo(UserRecord userRecord) {
        Assert.notNull(userRecord, "userRecord cannot be null");
        LOGGER.info("Finding primary ACHPaymentInfo for user:=> {}", userRecord.getEmbeddedContactInfo().getEmail());
        return iachPaymentInfoRepository.findPrimaryUserACHPaymentInfo(userRecord);
    }

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
    public void updateStripeBankAcct(BankAccount bankAccount, ACHPaymentInfo achPaymentInfo) {
        Assert.notNull(bankAccount, "stripeBankAccount cannot be null");
        Assert.notNull(achPaymentInfo, "achPaymentInfo cannot be null");

        LOGGER.debug("Updating Stripe BankAcct details on ACHPaymentInfo with ID:=> {}", achPaymentInfo.getId());

        // Get AccountHolderType if available
        StripeAcctHolderTypeE stripeAcctHolderTypeE = getStripeAcctHolderTypeE(bankAccount);

        StripeBankAcct stripeBankAcct = achPaymentInfo.getStripeBankAcct();
        if (stripeBankAcct == null) {
            stripeBankAcct = StripeBankAcct.builder()
                    .id(bankAccount.getId())
                    .object(bankAccount.getObject())
                    .accountHolderName(bankAccount.getAccountHolderName())
                    .accountHolderType(stripeAcctHolderTypeE)
                    .bankName(bankAccount.getBankName())
                    .country(bankAccount.getCountry())
                    .currency(bankAccount.getCurrency())
                    .last4(bankAccount.getLast4())
                    .routingNumber(bankAccount.getRoutingNumber())
                    .acctStatus(StripeBankAcctStatusE.getByStatus(bankAccount.getStatus()))
                    .build();
        } else {
            stripeBankAcct.setObject(bankAccount.getObject());
            stripeBankAcct.setAccountHolderName(bankAccount.getAccountHolderName());
            stripeBankAcct.setAccountHolderType(stripeAcctHolderTypeE);
            stripeBankAcct.setBankName(bankAccount.getBankName());
            stripeBankAcct.setCountry(bankAccount.getCountry());
            stripeBankAcct.setCurrency(bankAccount.getCurrency());
            stripeBankAcct.setLast4(bankAccount.getLast4());
            stripeBankAcct.setRoutingNumber(bankAccount.getRoutingNumber());
            stripeBankAcct.setAcctStatus(StripeBankAcctStatusE.getByStatus(bankAccount.getStatus()));
        }

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

    StripeAcctHolderTypeE getStripeAcctHolderTypeE(BankAccount bankAccount) {
        Optional<StripeAcctHolderTypeE> optionalStripeAcctHolderTypeE = StripeAcctHolderTypeE.getByHolderType(bankAccount.getAccountHolderType());
        StripeAcctHolderTypeE stripeAcctHolderTypeE = optionalStripeAcctHolderTypeE.isPresent() ? optionalStripeAcctHolderTypeE.get() : null;
        return stripeAcctHolderTypeE;
    }

}
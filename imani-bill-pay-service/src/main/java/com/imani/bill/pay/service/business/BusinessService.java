package com.imani.bill.pay.service.business;

import com.imani.bill.pay.domain.business.Business;
import com.imani.bill.pay.domain.business.repository.IBusinessRepository;
import com.imani.bill.pay.service.payment.stripe.IStripeAccountService;
import com.imani.bill.pay.service.payment.stripe.StripeAccountService;
import com.stripe.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * @author manyce400
 */
@Service(BusinessService.SPRING_BEAN)
public class BusinessService implements IBusinessService {


    @Autowired
    private IBusinessRepository iBusinessRepository;

    @Autowired
    @Qualifier(StripeAccountService.SPRING_BEAN)
    private IStripeAccountService iStripeAccountService;

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.business.BusinessService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(BusinessService.class);



    @Override
    public void registerBusiness(Business business) {
        Assert.notNull(business, "Business cannot be null");
        Assert.notNull(business.getId(), "New Business cannot be already persisted with an ID");

        LOGGER.info("Registering a new Imani BillPay Business[{}]", business);

        // First create a custom Connected Stripe Accte
        Optional<Account> account = iStripeAccountService.createConnectedStripeAcct(business);
        if(account.isPresent()) {
            business.setStripeAcctID(account.get().getId());
        }

        saveBusiness(business);
    }

    @Override
    public boolean removeConnectedStripeAcct(Business business) {
        Assert.notNull(business, "Business cannot be null");
        LOGGER.info("Removing connected Stripe Acct for Business[{}]", business.getName());

        boolean removed = iStripeAccountService.removeConnectedStripeAcct(business);
        if(removed) {
            saveBusiness(business);
        }

        return removed;
    }

    @Transactional
    @Override
    public void saveBusiness(Business business) {
        Assert.notNull(business, "Business cannot be null");
        LOGGER.debug("Saving business => {}", business);
        iBusinessRepository.save(business);
    }

}

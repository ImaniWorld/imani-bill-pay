package com.imani.bill.pay.service.business;

import com.imani.bill.pay.domain.business.Business;

/**
 * @author manyce400
 */
public interface IBusinessService {

    // Register a new Imani BillPay Business.
    public void registerBusiness(Business business);

    public boolean removeConnectedStripeAcct(Business business);

    // Save/update Imani BillPay Business
    public void saveBusiness(Business business);

}

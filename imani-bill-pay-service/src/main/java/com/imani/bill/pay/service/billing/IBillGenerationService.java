package com.imani.bill.pay.service.billing;

import com.imani.bill.pay.domain.user.UserRecord;

/**
 * Generic interface for all business specific Imani BillPay Bill generation implementations.
 *
 * @author manyce400
 */
public interface IBillGenerationService<O> {

    public boolean generateImaniBill(UserRecord userRecord);

    public boolean generateImaniBill(O generationObject);

}
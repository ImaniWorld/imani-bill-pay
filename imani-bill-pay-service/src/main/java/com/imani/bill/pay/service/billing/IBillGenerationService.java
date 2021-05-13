package com.imani.bill.pay.service.billing;

/**
 * Generic interface for all business specific Imani BillPay Bill generation implementations.
 *
 * @author manyce400
 */
public interface IBillGenerationService<O> {

    public boolean generateImaniBill(O generationObject);

}
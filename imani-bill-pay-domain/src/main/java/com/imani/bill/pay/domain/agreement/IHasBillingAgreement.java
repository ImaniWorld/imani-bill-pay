package com.imani.bill.pay.domain.agreement;

/**
 * @author manyce400
 */
public interface IHasBillingAgreement {

    public EmbeddedAgreement getEmbeddedAgreement();

    public String describeAgreement();

}

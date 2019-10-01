package com.imani.bill.pay.service.property;

import com.imani.bill.pay.domain.property.RentalAgreement;

/**
 * @author manyce400
 */
public interface IRentalAgreementService {

    public boolean isRentalAgreementInForce(RentalAgreement rentalAgreement);

}

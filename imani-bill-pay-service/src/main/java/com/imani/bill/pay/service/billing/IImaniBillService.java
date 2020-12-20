package com.imani.bill.pay.service.billing;

import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.user.UserRecord;

import java.util.Optional;

/**
 * @author manyce400
 */
public interface IImaniBillService {

    public Optional<ImaniBill> findByID(Long id);

    public Optional<ImaniBill> findByUserCurrentMonthResidentialLease(UserRecord userRecord);

    public void save(ImaniBill imaniBill);

}

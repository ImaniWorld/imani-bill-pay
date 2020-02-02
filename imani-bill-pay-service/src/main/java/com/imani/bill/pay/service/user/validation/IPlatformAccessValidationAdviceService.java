package com.imani.bill.pay.service.user.validation;

import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.service.validation.IValidationAdviceService;

/**
 * Implementations of this are responsible for verifying and providing advice results on if a user has
 * valid access to Imani BillPay platform.
 *
 * @author manyce400
 */
public interface IPlatformAccessValidationAdviceService extends IValidationAdviceService<UserRecord> {


}

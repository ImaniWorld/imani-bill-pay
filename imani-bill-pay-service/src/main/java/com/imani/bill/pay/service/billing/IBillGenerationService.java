package com.imani.bill.pay.service.billing;

import com.imani.bill.pay.domain.billing.ImaniBillExplained;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.UserRecordLite;

import java.util.Optional;

/**
 * Generic interface for all business specific Imani BillPay Bill generation implementations.
 *
 * @author manyce400
 */
public interface IBillGenerationService {

    public boolean generateImaniBill(UserRecord userRecord);

    public Optional<ImaniBillExplained> genCurrentBillExplanation(UserRecordLite userRecordLite);

    public Optional<ImaniBillExplained> genCurrentBillExplanation(UserRecord userRecord);

}
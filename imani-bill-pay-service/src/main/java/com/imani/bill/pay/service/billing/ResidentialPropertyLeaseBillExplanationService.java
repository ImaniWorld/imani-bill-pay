package com.imani.bill.pay.service.billing;

import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.billing.ImaniBillExplained;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.UserRecordLite;
import com.imani.bill.pay.domain.user.repository.IUserRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * @author manyce400
 */
@Service(ResidentialPropertyLeaseBillExplanationService.SPRING_BEAN)
public class ResidentialPropertyLeaseBillExplanationService implements IBillExplanationService {



    @Autowired
    private IUserRecordRepository iUserRecordRepository;

    @Autowired
    @Qualifier(ImaniBillService.SPRING_BEAN)
    private IImaniBillService imaniBillService;


    public static final String SPRING_BEAN = "com.imani.bill.pay.service.billing.ResidentialPropertyLeaseBillExplanationService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ResidentialPropertyLeaseBillExplanationService.class);


    @Override
    public Optional<ImaniBillExplained> getCurrentBillExplanation(UserRecord userRecord) {
        Assert.notNull(userRecord, "UserRecord cannot be null");
        LOGGER.info("Attempting to generate lease agreement bill for user: {} ", userRecord.getEmbeddedContactInfo().getEmail());

        Optional<ImaniBill> imaniBill = imaniBillService.findByUserCurrentMonthResidentialLease(userRecord);
        if(imaniBill.isPresent()) {
            return Optional.of(imaniBill.get().toImaniBillExplained());
        }

        return Optional.empty();
    }

    @Override
    public Optional<ImaniBillExplained> getCurrentBillExplanation(UserRecordLite userRecordLite) {
        Assert.notNull(userRecordLite, "UserRecordLite cannot be null");
        UserRecord userRecord = iUserRecordRepository.findByUserEmail(userRecordLite.getEmail());
        return getCurrentBillExplanation(userRecord);
    }


}

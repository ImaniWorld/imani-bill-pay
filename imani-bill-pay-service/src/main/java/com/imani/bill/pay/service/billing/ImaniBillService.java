package com.imani.bill.pay.service.billing;

import com.imani.bill.pay.domain.billing.BillScheduleTypeE;
import com.imani.bill.pay.domain.billing.BillServiceRenderedTypeE;
import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.billing.repository.IImaniBillRepository;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.service.util.DateTimeUtil;
import com.imani.bill.pay.service.util.IDateTimeUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * @author manyce400
 */
@Service(ImaniBillService.SPRING_BEAN)
public class ImaniBillService implements IImaniBillService {


    @Autowired
    private IImaniBillRepository imaniBillRepository;

    @Autowired
    @Qualifier(DateTimeUtil.SPRING_BEAN)
    private IDateTimeUtil iDateTimeUtil;

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.billing.ImaniBillService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ImaniBillService.class);



    @Override
    public Optional<ImaniBill> findByID(Long id) {
        Assert.notNull(id, "id cannot be null");
        LOGGER.debug("Finding ImaniBill with id:=> {}", id);
        return imaniBillRepository.findById(id);
    }


    @Override
    public Optional<ImaniBill> findByUserCurrentMonthResidentialLease(UserRecord userRecord) {
        Assert.notNull(userRecord, "userRecord cannot be null");

        DateTime dateTimeAtStartOfMonth = iDateTimeUtil.getDateTimeAtStartOfMonth(DateTime.now());
        String dateString = iDateTimeUtil.toDisplayFriendlyNoTime(dateTimeAtStartOfMonth);

        LOGGER.info("Finding current month: {} residential lease bill for user: {}", userRecord.getEmbeddedContactInfo().getEmail(),  dateString);
        Optional<ImaniBill> imaniBill = imaniBillRepository.getImaniBill(userRecord, dateTimeAtStartOfMonth, BillScheduleTypeE.MONTHLY, BillServiceRenderedTypeE.Residential_Lease);
        return imaniBill;
    }

    @Override
    public void save(ImaniBill imaniBill) {
        Assert.notNull(imaniBill, "ImaniBill cannot be null");
        LOGGER.debug("Saving ImaniBill with id:=> {}", imaniBill.getId());
        imaniBillRepository.save(imaniBill);
    }

}

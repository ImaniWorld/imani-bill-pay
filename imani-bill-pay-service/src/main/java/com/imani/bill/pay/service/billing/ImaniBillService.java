package com.imani.bill.pay.service.billing;

import com.imani.bill.pay.domain.agreement.EmbeddedAgreement;
import com.imani.bill.pay.domain.billing.BillServiceRenderedTypeE;
import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.billing.repository.IImaniBillRepository;
import com.imani.bill.pay.domain.education.TuitionAgreement;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.service.util.DateTimeUtil;
import com.imani.bill.pay.service.util.IDateTimeUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
    public boolean isBillPaymentLate(ImaniBill imaniBill, EmbeddedAgreement embeddedAgreement) {
        Assert.notNull(imaniBill, "ImaniBill cannot be null");
        Assert.notNull(embeddedAgreement, "EmbeddedAgreement cannot be null");

        // Get scheduled date ant the number of days till considered late on the agreement
        DateTime billScheduleDate = imaniBill.getBillScheduleDate();
        Integer maxDaysTillLate = embeddedAgreement.getNumberOfDaysTillLate();

        Integer daysBetweenDueAndNow = iDateTimeUtil.getDaysBetweenDates(billScheduleDate, DateTime.now());
        LOGGER.info("Late check. maxDaysTillLate => {} daysBetweenDueAndNow => {}", maxDaysTillLate, daysBetweenDueAndNow);
        return daysBetweenDueAndNow > maxDaysTillLate;
    }

    @Override
    public Optional<ImaniBill> findByUserCurrentMonthBill(UserRecord userRecord, BillServiceRenderedTypeE billServiceRenderedTypeE) {
        Assert.notNull(userRecord, "userRecord cannot be null");

        DateTime dateTimeAtStartOfMonth = iDateTimeUtil.getDateTimeAtStartOfMonth(DateTime.now());
        String dateString = iDateTimeUtil.toDisplayFriendlyNoTime(dateTimeAtStartOfMonth);

        // Using Fetch records from repository object to load all bill payment records for this bill
        LOGGER.info("Finding Current_Month[{}] BillServiceRenderedTypeE[{}]  for user: {}", dateString, billServiceRenderedTypeE, userRecord.getEmbeddedContactInfo().getEmail());
        Optional<ImaniBill> imaniBill = Optional.empty();//imaniBillRepository.getImaniBillFetchRecords(userRecord, dateTimeAtStartOfMonth, BillScheduleTypeE.MONTHLY, billServiceRenderedTypeE);
        return imaniBill;
    }

    @Override
    public Set<ImaniBill> findYTDResidentialPropertyLeaseBills(UserRecord userRecord) {
        Assert.notNull(userRecord, "userRecord cannot be null");

        DateTime atStartOfYear = iDateTimeUtil.getDateTimeAtStartOfYear(DateTime.now());
        DateTime dateTimeAtStartOfCurrentMonth = iDateTimeUtil.getDateTimeAtStartOfMonth(DateTime.now());

        String start = iDateTimeUtil.toDisplayFriendlyNoTime(atStartOfYear);
        String end = iDateTimeUtil.toDisplayFriendlyNoTime(dateTimeAtStartOfCurrentMonth);

        LOGGER.info("Finding entire YTD residential property lease bills for User: {} between [{} - {}]", userRecord.getEmbeddedContactInfo().getEmail(), start, end);
        Set<ImaniBill> imaniBills =  new HashSet<>();//imaniBillRepository.getYTDImaniBillsFetchRecords(userRecord, atStartOfYear, dateTimeAtStartOfCurrentMonth, BillScheduleTypeE.MONTHLY, BillServiceRenderedTypeE.Residential_Lease);
        return imaniBills;
    }

    @Override
    public Set<ImaniBill> findYTDUnPaidImaniBillsForUser(UserRecord userRecord, TuitionAgreement tuitionAgreement) {
        Assert.notNull(userRecord, "userRecord cannot be null");
        Assert.notNull(tuitionAgreement, "tuitionAgreement cannot be null");

        DateTime atStartOfYear = iDateTimeUtil.getDateTimeAtStartOfYear(DateTime.now());
        DateTime atEndOfYear = iDateTimeUtil.getDateTimeAtEndOfYear(DateTime.now());

        String start = iDateTimeUtil.toDisplayFriendlyNoTime(atStartOfYear);
        String end = iDateTimeUtil.toDisplayFriendlyNoTime(atEndOfYear);

        LOGGER.info("Finding entire YTD unpaid tuition agreement bills for User: {} between [{} - {}]", userRecord.getEmbeddedContactInfo().getEmail(), start, end);
        return new HashSet<>();//imaniBillRepository.getYTDUnPaidImaniBillsForUser(userRecord, atStartOfYear, atEndOfYear, tuitionAgreement);
    }

    @Override
    public Optional<ImaniBill> findCurrentMonthBillForTuitionAgreement(UserRecord userRecord, TuitionAgreement tuitionAgreement) {
        Assert.notNull(userRecord, "userRecord cannot be null");
        Assert.notNull(tuitionAgreement, "billServiceRenderedTypeE cannot be null");

        DateTime dateTimeAtStartOfMonth = iDateTimeUtil.getDateTimeAtStartOfMonth(DateTime.now());
        String dateString = iDateTimeUtil.toDisplayFriendlyNoTime(dateTimeAtStartOfMonth);

        LOGGER.info("Finding current month({}) TuitionAgreement[ID: {}] bill for User => {}", dateString, tuitionAgreement.getId(), userRecord.getEmbeddedContactInfo().getEmail());
        Optional<ImaniBill> imaniBill = Optional.empty();//imaniBillRepository.getImaniBillForTuitionAndUser(userRecord, tuitionAgreement);
        return imaniBill;
    }

//    @Override
//    public Optional<ImaniBill> findCurrentMonthBillByServiceRendered(UserRecord userRecord, BillServiceRenderedTypeE billServiceRenderedTypeE) {
//        Assert.notNull(userRecord, "userRecord cannot be null");
//        Assert.notNull(billServiceRenderedTypeE, "billServiceRenderedTypeE cannot be null");
//
//        DateTime dateTimeAtStartOfMonth = iDateTimeUtil.getDateTimeAtStartOfMonth(DateTime.now());
//        String dateString = iDateTimeUtil.toDisplayFriendlyNoTime(dateTimeAtStartOfMonth);
//
//        LOGGER.info("Finding current month({}) [{}] bill for user => {}", dateString, billServiceRenderedTypeE, userRecord.getEmbeddedContactInfo().getEmail());
//        Optional<ImaniBill> imaniBill = imaniBillRepository.getImaniBillFetchRecords(userRecord, dateTimeAtStartOfMonth, BillScheduleTypeE.MONTHLY, billServiceRenderedTypeE);
//        return imaniBill;
//    }

    @Override
    public void save(ImaniBill imaniBill) {
        Assert.notNull(imaniBill, "ImaniBill cannot be null");
        LOGGER.debug("Saving ImaniBill with id:=> {}", imaniBill.getId());
        imaniBillRepository.save(imaniBill);
    }

}

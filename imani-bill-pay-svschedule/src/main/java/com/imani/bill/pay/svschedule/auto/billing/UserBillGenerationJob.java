package com.imani.bill.pay.svschedule.auto.billing;

import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.UserRecordTypeE;
import com.imani.bill.pay.domain.user.repository.IUserRecordRepository;
import com.imani.bill.pay.service.billing.IBillGenerationService;
import com.imani.bill.pay.service.billing.PropertyLeaseBillGenerationService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author manyce400
 */
@Component
public class UserBillGenerationJob implements Job {


    @Autowired
    private IUserRecordRepository iUserRecordRepository;

    @Autowired
    @Qualifier(PropertyLeaseBillGenerationService.SPRING_BEAN)
    private IBillGenerationService iBillGenerationService;

    private static final String JOB_DETAIL = "USER-BILL-GENERATION-";

    private static final String JOB_TRIGGER = "USER-BILL-GENERATION-TRIGGER";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(UserBillGenerationJob.class);


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOGGER.info("Running automated Imani BillPay generation.....");

        List<UserRecord> leaseUsers = iUserRecordRepository.findAllUsersByType(UserRecordTypeE.Tenant);

        for(UserRecord userRecord : leaseUsers) {
            try {
                iBillGenerationService.generateImaniBill(userRecord);
            } catch (Exception e) {
                LOGGER.warn("Failed to generate bill for user:=> {}", userRecord.getEmbeddedContactInfo().getEmail());
                e.printStackTrace();
            }
        }
    }

    @Bean(UserBillGenerationJob.JOB_DETAIL)
    public JobDetail jobDetail() {
        return JobBuilder.newJob().ofType(UserBillGenerationJob.class)
                .storeDurably()
                .withIdentity("Auto_BillPay_Generation")
                .withDescription("Invoke BillPay job to auto generate user billing.")
                .build();
    }

    @Bean(UserBillGenerationJob.JOB_TRIGGER)
    public Trigger trigger(@Qualifier(UserBillGenerationJob.JOB_DETAIL) JobDetail job) {
        return TriggerBuilder.newTrigger().forJob(job)
                .withIdentity("Qrtz_Trigger")
                .withDescription("Sample trigger")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().repeatForever().withIntervalInSeconds(20))
                .build();
    }


}

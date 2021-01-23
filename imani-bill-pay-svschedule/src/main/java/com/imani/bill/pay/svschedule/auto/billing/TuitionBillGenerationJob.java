package com.imani.bill.pay.svschedule.auto.billing;

import com.imani.bill.pay.domain.user.repository.IUserRecordRepository;
import com.imani.bill.pay.service.billing.IBillGenerationService;
import com.imani.bill.pay.service.billing.TuitionBillGenerationService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 *
 *
 * @author manyce400
 */
@Component
public class TuitionBillGenerationJob implements Job {


    @Autowired
    private IUserRecordRepository iUserRecordRepository;

    @Autowired
    @Qualifier(TuitionBillGenerationService.SPRING_BEAN)
    private IBillGenerationService iBillGenerationService;

    private static final String JOB_DETAIL = "TUITION-BILL-GENERATION-JOB";

    private static final String JOB_TRIGGER = "TUITION-BILL-GENERATION-TRIGGER";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(TuitionBillGenerationJob.class);


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        LOGGER.info("Running automated Imani BillPay Tuition Bill generation .....");
//
//        // Find all BillPayer user's in order to generate TuitionAgreement bills for them
//        List<UserRecord> billPayerUsers = iUserRecordRepository.findAllUsersByType(UserRecordTypeE.BillPayer);
//
//        for(UserRecord billPayer : billPayerUsers) {
//            try {
//                iBillGenerationService.generateImaniBill(billPayer);
//            } catch (Exception e) {
//                LOGGER.warn("Failed to generate bill for user:=> {}", billPayer.getEmbeddedContactInfo().getEmail(), e);
//            }
//        }
    }

    @Bean(TuitionBillGenerationJob.JOB_DETAIL)
    public JobDetail jobDetail() {
        return JobBuilder.newJob().ofType(TuitionBillGenerationJob.class)
                .storeDurably()
                .withIdentity("Auto_Tuition_Bill_Generation")
                .withDescription("Job to generate all ImaniBillPay TuitionAgreement Bills")
                .build();
    }

    @Bean(TuitionBillGenerationJob.JOB_TRIGGER)
    public Trigger trigger(@Qualifier(TuitionBillGenerationJob.JOB_DETAIL) JobDetail job) {
        return TriggerBuilder.newTrigger().forJob(job)
                .withIdentity("Tuition_Bill_Trigger")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().repeatForever().withIntervalInSeconds(20))
                .build();
    }


}

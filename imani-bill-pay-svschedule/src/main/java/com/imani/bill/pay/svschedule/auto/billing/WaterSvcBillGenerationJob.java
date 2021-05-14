package com.imani.bill.pay.svschedule.auto.billing;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.imani.bill.pay.domain.utility.WaterServiceAgreement;
import com.imani.bill.pay.domain.utility.repository.IWaterServiceAgreementRepository;
import com.imani.bill.pay.service.billing.IBillGenerationService;
import com.imani.bill.pay.service.billing.utility.WaterSvcBillGenerationService;
import com.imani.bill.pay.service.concurrency.AppConcurrencyConfigurator;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 *
 * @author manyce400
 */
@Component
public class WaterSvcBillGenerationJob implements Job {


    @Autowired
    @Qualifier(WaterSvcBillGenerationService.SPRING_BEAN)
    private IBillGenerationService<WaterServiceAgreement> iBillGenerationService;

    @Autowired
    @Qualifier(AppConcurrencyConfigurator.AUTO_BILLING_THREAD_POOL)
    private ListeningExecutorService listeningExecutorService;

    @Autowired
    private IWaterServiceAgreementRepository iWaterServiceAgreementRepository;

    private static final String JOB_DETAIL = "WATER-SVC-BILL-GENERATION-JOB";

    private static final String JOB_TRIGGER = "WATER-SVC-BILL-GENERATION-TRIGGER";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(WaterSvcBillGenerationJob.class);


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOGGER.info("Kicking off Imani BillPay automated billing services....");

        // Find all BillPayer user's in order to generate TuitionAgreement bills for them
        List<WaterServiceAgreement> waterServiceAgreements = iWaterServiceAgreementRepository.findAllWhereAgreementInforce();
        waterServiceAgreements.forEach(waterServiceAgreement -> {
            // Kick off each water billing generation on a different thread to free up Quartz threads for further processing
            Runnable runnable =() -> {
                iBillGenerationService.generateImaniBill(waterServiceAgreement);
            };

            listeningExecutorService.submit(runnable);
        });
    }

    @Bean(WaterSvcBillGenerationJob.JOB_DETAIL)
    public JobDetail jobDetail() {
        return JobBuilder.newJob().ofType(WaterSvcBillGenerationJob.class)
                .storeDurably()
                .withIdentity("Auto_Water_Service_Bill_Generation")
                .build();
    }

    @Bean(WaterSvcBillGenerationJob.JOB_TRIGGER)
    public Trigger trigger(@Qualifier(WaterSvcBillGenerationJob.JOB_DETAIL) JobDetail job) {
        return TriggerBuilder.newTrigger().forJob(job)
                .withIdentity("WATER-SVC_Bill_Trigger")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().repeatForever().withIntervalInSeconds(60))
                .build();
    }


}

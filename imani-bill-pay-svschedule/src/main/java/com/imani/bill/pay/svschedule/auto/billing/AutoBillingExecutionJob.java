package com.imani.bill.pay.svschedule.auto.billing;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.imani.bill.pay.domain.utility.SewerServiceAgreement;
import com.imani.bill.pay.domain.utility.WaterServiceAgreement;
import com.imani.bill.pay.domain.utility.repository.ISewerServiceAgreementRepository;
import com.imani.bill.pay.domain.utility.repository.IWaterServiceAgreementRepository;
import com.imani.bill.pay.service.billing.IBillGenerationService;
import com.imani.bill.pay.service.billing.utility.SewerBillGenerationService;
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
public class AutoBillingExecutionJob implements Job {



    @Autowired
    private IWaterServiceAgreementRepository iWaterServiceAgreementRepository;

    @Autowired
    private ISewerServiceAgreementRepository iSewerServiceAgreementRepository;

    @Autowired
    @Qualifier(WaterSvcBillGenerationService.SPRING_BEAN)
    private IBillGenerationService<WaterServiceAgreement> iWaterBillGenerationService;

    @Autowired
    @Qualifier(SewerBillGenerationService.SPRING_BEAN)
    private IBillGenerationService<SewerServiceAgreement> iSewerBillGenerationService;

    @Autowired
    @Qualifier(AppConcurrencyConfigurator.AUTO_BILLING_THREAD_POOL)
    private ListeningExecutorService listeningExecutorService;


    private static final String JOB_DETAIL = "AUTOMATED-BILL-GENERATION-JOB";

    private static final String JOB_TRIGGER = "AUTOMATED-BILL-GENERATION-TRIGGER";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(AutoBillingExecutionJob.class);


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //execWaterBilling();
        //execSewerBilling();
    }

    private void execWaterBilling() {
        List<WaterServiceAgreement> waterServiceAgreements = iWaterServiceAgreementRepository.findAllWhereAgreementInforce();
        waterServiceAgreements.forEach(waterServiceAgreement -> {
            // Kick off each water billing generation on a different thread to free up Quartz threads for further processing
            Runnable runnable =() -> {
                iWaterBillGenerationService.generateImaniBill(waterServiceAgreement);
            };

            listeningExecutorService.submit(runnable);
        });
    }

    private void execSewerBilling() {
        List<SewerServiceAgreement> sewerServiceAgreements = iSewerServiceAgreementRepository.findAllWhereAgreementInforce();
        sewerServiceAgreements.forEach(sewerServiceAgreement -> {
            Runnable runnable =() -> {
                iSewerBillGenerationService.generateImaniBill(sewerServiceAgreement);
            };

            listeningExecutorService.submit(runnable);
        });
    }

    @Bean(AutoBillingExecutionJob.JOB_DETAIL)
    public JobDetail jobDetail() {
        return JobBuilder.newJob().ofType(AutoBillingExecutionJob.class)
                .storeDurably()
                .withIdentity("Auto_Bill_Generation")
                .build();
    }

    @Bean(AutoBillingExecutionJob.JOB_TRIGGER)
    public Trigger trigger(@Qualifier(AutoBillingExecutionJob.JOB_DETAIL) JobDetail job) {
        return TriggerBuilder.newTrigger().forJob(job)
                .withIdentity("AUTO_BillING_Trigger")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().repeatForever().withIntervalInSeconds(60))
                .build();
    }


}

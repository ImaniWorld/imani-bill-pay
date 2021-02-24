package com.imani.bill.pay.service.billing;

import com.imani.bill.pay.domain.billing.BillServiceRenderedTypeE;
import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.billing.ImaniBillExplained;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.execution.ValidationAdvice;
import com.imani.bill.pay.domain.user.UserRecord;
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
public class ResidentialPropertyLeaseBillExplanationService implements IBillExplanationService<UserRecord> {



    @Autowired
    private IUserRecordRepository iUserRecordRepository;

    @Autowired
    @Qualifier(ImaniBillService.SPRING_BEAN)
    private IImaniBillService imaniBillService;


    public static final String SPRING_BEAN = "com.imani.bill.pay.service.billing.ResidentialPropertyLeaseBillExplanationService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ResidentialPropertyLeaseBillExplanationService.class);


    @Override
    public ExecutionResult<ImaniBillExplained> getCurrentBillExplanation(UserRecord userRecord) {
        Assert.notNull(userRecord, "UserRecord cannot be null");
        LOGGER.info("Attempting to generate residential property lease agreement bill for user: {} ", userRecord.getEmbeddedContactInfo().getEmail());

        ExecutionResult<ImaniBillExplained> executionResult = new ExecutionResult<>();
        Optional<ImaniBill> imaniBill = imaniBillService.findByUserCurrentMonthBill(userRecord, BillServiceRenderedTypeE.Residential_Lease);

        if(imaniBill.isPresent()) {
            ImaniBillExplained imaniBillExplained = imaniBill.get().toImaniBillExplained();
            executionResult.setResult(imaniBillExplained);
        } else {
            executionResult.addValidationAdvice(ValidationAdvice.newInstance("No lease agreement bill found for user"));
        }

        return executionResult;
    }

//    @Override
//    public ExecutionResult<ImaniBillExplained> getCurrentBillExplanation(UserRecordLite userRecordLite) {
//        Assert.notNull(userRecordLite, "UserRecordLite cannot be null");
//        UserRecord userRecord = iUserRecordRepository.findByUserEmail(userRecordLite.getEmail());
//        return getCurrentBillExplanation(userRecord);
//    }
//
//    @Override
//    public ExecutionResult<List<ImaniBillExplained>> getYTDBillsExplanation(UserRecord userRecord) {
//        Assert.notNull(userRecord, "UserRecord cannot be null");
//        ExecutionResult<List<ImaniBillExplained>> executionResult = new ExecutionResult<>();
//        Set<ImaniBill> imaniBills = imaniBillService.findYTDResidentialPropertyLeaseBills(userRecord);
//        buildYTDExecutionResult(executionResult, imaniBills);
//        return executionResult;
//    }
//
//    @Override
//    public ExecutionResult<List<ImaniBillExplained>> getYTDBillsExplanation(UserRecordLite userRecordLite) {
//        Assert.notNull(userRecordLite, "UserRecordLite cannot be null");
//        UserRecord userRecord = iUserRecordRepository.findByUserEmail(userRecordLite.getEmail());
//        return getYTDBillsExplanation(userRecord);
//    }
//
//    private void buildYTDExecutionResult(ExecutionResult<List<ImaniBillExplained>> executionResult, Set<ImaniBill> imaniBills) {
//        if(!imaniBills.isEmpty()) {
//            List<ImaniBillExplained> imaniBillExplainedList = new ArrayList<>();
//            imaniBills.forEach(imaniBill -> {
//                ImaniBillExplained imaniBillExplained = imaniBill.toImaniBillExplained();
//                imaniBillExplainedList.add(imaniBillExplained);
//            });
//            executionResult.setResult(imaniBillExplainedList);
//        } else {
//            executionResult.addValidationAdvice(ValidationAdvice.newInstance("No billing activity YTD found."));
//        }
//    }
}

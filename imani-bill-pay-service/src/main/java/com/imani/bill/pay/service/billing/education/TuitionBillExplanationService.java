package com.imani.bill.pay.service.billing.education;

import com.imani.bill.pay.domain.billing.BillServiceRenderedTypeE;
import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.billing.ImaniBillExplained;
import com.imani.bill.pay.domain.education.TuitionAgreement;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.execution.ValidationAdvice;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.UserRecordLite;
import com.imani.bill.pay.domain.user.repository.IUserRecordRepository;
import com.imani.bill.pay.service.billing.IBillExplanationService;
import com.imani.bill.pay.service.billing.IImaniBillService;
import com.imani.bill.pay.service.billing.ImaniBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

/**
 * @author manyce400
 */
@Service(TuitionBillExplanationService.SPRING_BEAN)
public class TuitionBillExplanationService implements IBillExplanationService {




    @Autowired
    private IUserRecordRepository iUserRecordRepository;

    @Autowired
    @Qualifier(ImaniBillService.SPRING_BEAN)
    private IImaniBillService imaniBillService;


    public static final String SPRING_BEAN = "com.imani.bill.pay.service.billing.education.TuitionBillExplanationService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(TuitionBillExplanationService.class);


    @Override
    public ExecutionResult<ImaniBillExplained> getCurrentBillExplanation(UserRecord userRecord) {
        Assert.notNull(userRecord, "UserRecord cannot be null");
        LOGGER.info("Attempting to generate TuitionAgreement Bill Explanation for User[{}] ", userRecord.getEmbeddedContactInfo().getEmail());

        ExecutionResult<ImaniBillExplained> executionResult = new ExecutionResult<>();
        Optional<ImaniBill> imaniBill = imaniBillService.findByUserCurrentMonthBill(userRecord, BillServiceRenderedTypeE.Tuition);

        Optional<ImaniBillExplained> billExplained = explainImaniBill(imaniBill);
        if(billExplained.isPresent()) {
            TuitionAgreement tuitionAgreement = imaniBill.get().getTuitionAgreement();
            billExplained.get().getBillPurposeExplained().setStudent(tuitionAgreement.getStudent().getFullName());
            executionResult.setResult(billExplained.get());
        } else {
            executionResult.addValidationAdvice(ValidationAdvice.newInstance("No TuitionAgreement bill found for user"));
        }

        return executionResult;
    }

    @Override
    public ExecutionResult<ImaniBillExplained> getCurrentBillExplanation(UserRecordLite userRecordLite) {
        Assert.notNull(userRecordLite, "UserRecordLite cannot be null");
        UserRecord userRecord = iUserRecordRepository.findByUserEmail(userRecordLite.getEmail());
        return getCurrentBillExplanation(userRecord);
    }

    @Override
    public ExecutionResult<List<ImaniBillExplained>> getYTDBillsExplanation(UserRecord userRecord) {
        return null;
    }

    @Override
    public ExecutionResult<List<ImaniBillExplained>> getYTDBillsExplanation(UserRecordLite userRecordLite) {
        return null;
    }
}

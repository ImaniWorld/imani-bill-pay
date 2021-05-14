package com.imani.bill.pay.xecservice.utility;

import com.imani.bill.pay.domain.billing.BillPayFee;
import com.imani.bill.pay.domain.billing.repository.IBillPayFeeRepository;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.utility.SewerServiceAgreement;
import com.imani.bill.pay.service.utility.ISewerSvcAgreementService;
import com.imani.bill.pay.service.utility.SewerSvcAgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author manyce400
 */
@Service(SewerSvcAgreementExecService.SPRING_BEAN)
public class SewerSvcAgreementExecService implements ISewerSvcAgreementExecService {


    @Autowired
    private IBillPayFeeRepository iBillPayFeeRepository;

    @Autowired
    @Qualifier(SewerSvcAgreementService.SPRING_BEAN)
    private ISewerSvcAgreementService iSewerSvcAgreementService;

    public static final String SPRING_BEAN = "com.imani.bill.pay.xecservice.utility.SewerSvcAgreementExecService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(SewerSvcAgreementExecService.class);

    @Override
    public void processSewerSvcAgreement(ExecutionResult<SewerServiceAgreement> executionResult, List<BillPayFee> billPayFees) {
        if(!executionResult.hasValidationAdvice()) {
            LOGGER.debug("Passed initial Advisor validation, invoking service bean to create sewer agreement...");

            List<BillPayFee> persistedBillPayFees = new ArrayList<>();
            for(BillPayFee billPayFee : billPayFees) {
                Optional<BillPayFee> persistedBillPayFee = iBillPayFeeRepository.findById(billPayFee.getId());
                if(persistedBillPayFee.isPresent()) {
                    persistedBillPayFees.add(persistedBillPayFee.get());
                }
            }

            iSewerSvcAgreementService.createAgreement(executionResult.getResult().get(), executionResult, persistedBillPayFees);
        }
    }

}
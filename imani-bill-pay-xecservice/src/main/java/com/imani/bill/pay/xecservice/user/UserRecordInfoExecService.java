package com.imani.bill.pay.xecservice.user;

import com.imani.bill.pay.domain.business.Business;
import com.imani.bill.pay.domain.business.repository.IBusinessRepository;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.user.UserRecordLite;
import com.imani.bill.pay.domain.user.UserRecordTypeE;
import com.imani.bill.pay.service.user.IUserToBusinessService;
import com.imani.bill.pay.service.user.UserToBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

/**
 * Default implementation.  Note that all methods in this class have an AspectJ before() advice being executed before each invocation
 *
 * @see UserRecordInfoExecAdvisor
 * @author manyce400
 */
@Service(UserRecordInfoExecService.SPRING_BEAN)
public class UserRecordInfoExecService implements IUserRecordInfoExecService {


    @Autowired
    private IBusinessRepository iBusinessRepository;

    @Autowired
    @Qualifier(UserToBusinessService.SPRING_BEAN)
    private IUserToBusinessService iUserToBusinessService;

    public static final String SPRING_BEAN = "com.imani.bill.pay.xservice.user.UserRecordInfoExecService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(UserRecordInfoExecService.class);

    @Override
    public void findUsersWithBusinessAffiliation(Long businessID, String userType, ExecutionResult<List<UserRecordLite>> executionResult) {
        Assert.notNull(businessID, "Business cannot be null");
        Assert.notNull(userType, "userType cannot be null");
        Assert.notNull(executionResult, "userRecordTypeE cannot be null");

        if(!executionResult.hasValidationAdvice()
                && !executionResult.hasExecutionError()) {
            Optional<Business> business = iBusinessRepository.findById(businessID);
            Optional<UserRecordTypeE> userRecordTypeE = UserRecordTypeE.findByType(userType);
            LOGGER.info("Attempting to find all Imani BillPay users affiliated with Business[{}] and UserRecordTypeE[{}]", business.get().getName(), userRecordTypeE.get());
            List<UserRecordLite> usersWithBusinessAffiliation = iUserToBusinessService.findUsersWithBusinessAffiliation(business.get(), userRecordTypeE.get());
            executionResult.setResult(usersWithBusinessAffiliation);
        } else {
            LOGGER.warn("Validation and execution issues found, skipping finding users with business affiliation");
        }
    }

}

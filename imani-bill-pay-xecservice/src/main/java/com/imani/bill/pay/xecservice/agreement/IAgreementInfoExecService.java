package com.imani.bill.pay.xecservice.agreement;

import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.utility.WaterServiceAgreementLite;

import java.util.List;

/**
 * @author manyce400
 */
public interface IAgreementInfoExecService {

    // Finds and loads all the active water service agreements which are in force for all properties in a community
    public void findCommunityPropertiesWaterAgreements(Long communityID, ExecutionResult<List<WaterServiceAgreementLite>> executionResult);

}

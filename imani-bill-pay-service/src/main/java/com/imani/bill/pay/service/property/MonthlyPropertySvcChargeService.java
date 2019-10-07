package com.imani.bill.pay.service.property;

import com.imani.bill.pay.domain.property.MonthlyRentalBill;
import com.imani.bill.pay.domain.property.PropertyService;
import com.imani.bill.pay.domain.property.PropertyServiceChargeExplained;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.UserResidence;
import com.imani.bill.pay.domain.user.UserResidencePropertyService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author manyce400
 */
@Service(MonthlyPropertySvcChargeService.SPRING_BEAN)
public class MonthlyPropertySvcChargeService implements IMonthlyPropertySvcChargeService {



    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MonthlyPropertySvcChargeService.class);

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.property.MonthlyPropertySvcChargeService";


    @Override
    public Optional<List<PropertyServiceChargeExplained>> applyMonthlyPropertyServiceCharge(UserResidence userResidence, MonthlyRentalBill monthlyRentalBill) {
        Assert.notNull(userResidence, "UserResidence cannot be null");
        Assert.notNull(monthlyRentalBill, "MonthlyRentalBill cannot be null");

        UserRecord userRecord = userResidence.getUserRecord();

        List<PropertyServiceChargeExplained> propertyServiceChargeExplainedList = new ArrayList<>();

        // Get all the PropertyServices that this user has signed up for
        Set<UserResidencePropertyService> userResidencePropertyServiceSet = userResidence.getUserResidencePropertyServices();

        userResidencePropertyServiceSet.forEach(userResidencePropertyService -> {
            if (userResidencePropertyService.isActive()) {
                PropertyService propertyService = userResidencePropertyService.getPropertyService();
                LOGGER.info("Applying PropertyService: {} for user: {}", propertyService.getServiceName(), userRecord.getEmbeddedContactInfo().getEmail());
                Double cost = propertyService.getServiceMonthlyCost();

                PropertyServiceChargeExplained propertyServiceChargeExplained = PropertyServiceChargeExplained.builder()
                        .serviceMonthlyCost(propertyService.getServiceMonthlyCost())
                        .serviceName(propertyService.getServiceName())
                        .signedUpDate(propertyService.getCreateDate())
                        .build();
                propertyServiceChargeExplainedList.add(propertyServiceChargeExplained);
            }
        });

        if(propertyServiceChargeExplainedList.size() > 0) {
            return Optional.of(propertyServiceChargeExplainedList);
        }

        return Optional.empty();
    }
}

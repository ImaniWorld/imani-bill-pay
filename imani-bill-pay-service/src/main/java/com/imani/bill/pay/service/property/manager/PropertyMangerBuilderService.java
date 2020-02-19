package com.imani.bill.pay.service.property.manager;

import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.execution.ValidationAdvice;
import com.imani.bill.pay.domain.property.PropertyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author manyce400
 */
@Service(PropertyMangerBuilderService.SPRING_BEAN)
public class PropertyMangerBuilderService implements IPropertyMangerBuilderService {



    @Autowired
    @Qualifier(PropertyManagerService.SPRING_BEAN)
    private IPropertyManagerService iPropertyManagerService;


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PropertyMangerBuilderService.class);

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.property.manager.PropertyMangerBuilderService";


    @Override
    public ExecutionResult newPropertyManger(PropertyManager propertyManager) {
        Assert.notNull(propertyManager, "PropertyManager cannot be null");

        LOGGER.info("Building new PropertyManager with name:=> {}", propertyManager.getName());

        ExecutionResult executionResult = new ExecutionResult();

        // Check to see if there are any PropertyManager registered with the same email
        PropertyManager jpaPropertyManager = iPropertyManagerService.findByEmail(propertyManager.getEmbeddedContactInfo().getEmail());
        if(jpaPropertyManager == null) {
            iPropertyManagerService.save(propertyManager);
        } else {
            executionResult.addValidationAdvice(ValidationAdvice.newInstance("Property Manager already exists with email passed"));
        }

        return executionResult;
    }


}

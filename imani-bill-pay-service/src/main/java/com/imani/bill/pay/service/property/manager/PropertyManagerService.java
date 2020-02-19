package com.imani.bill.pay.service.property.manager;

import com.imani.bill.pay.domain.property.PropertyManager;
import com.imani.bill.pay.domain.property.repository.IPropertyManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author manyce400
 */
@Service(PropertyManagerService.SPRING_BEAN)
public class PropertyManagerService implements IPropertyManagerService {



    @Autowired
    private IPropertyManagerRepository iPropertyManagerRepository;



    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PropertyManagerService.class);

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.property.manager.PropertyManagerService";


    @Override
    public PropertyManager findByEmail(String email) {
        Assert.notNull(email, "Email cannot be null");
        LOGGER.debug("Finding PropertyManager with email:=> {}", email);
        return iPropertyManagerRepository.findByEmail(email);
    }

    @Override
    public void save(PropertyManager propertyManager) {
        Assert.notNull(propertyManager, "propertyManager cannot be null");
        LOGGER.debug("Saving propertyManager :=> {]", propertyManager);
        iPropertyManagerRepository.save(propertyManager);
    }

}

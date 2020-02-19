package com.imani.bill.pay.service.property.manager;

import com.imani.bill.pay.domain.property.PropertyManager;

/**
 * @author manyce400
 */
public interface IPropertyManagerService {


    public PropertyManager findByEmail(String email);

    public void save(PropertyManager propertyManager);

}

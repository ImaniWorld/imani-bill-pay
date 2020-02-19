package com.imani.bill.pay.service.property.manager;

import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.property.PropertyManager;

/**
 * @author manyce400
 */
public interface IPropertyMangerBuilderService {


    public ExecutionResult newPropertyManger(PropertyManager propertyManager);
}

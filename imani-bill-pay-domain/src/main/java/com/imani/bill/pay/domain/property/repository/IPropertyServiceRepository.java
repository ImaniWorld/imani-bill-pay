package com.imani.bill.pay.domain.property.repository;

import com.imani.bill.pay.domain.property.PropertyService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author manyce400
 */
@Repository
public interface IPropertyServiceRepository extends JpaRepository<PropertyService, Long> {


}

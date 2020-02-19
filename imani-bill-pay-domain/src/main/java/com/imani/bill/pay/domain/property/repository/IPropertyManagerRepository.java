package com.imani.bill.pay.domain.property.repository;

import com.imani.bill.pay.domain.property.PropertyManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author manyce400
 */
@Repository
public interface IPropertyManagerRepository extends JpaRepository<PropertyManager, Long> {

    @Query("Select propertyManager From PropertyManager propertyManager Where propertyManager.embeddedContactInfo.email = ?1")
    public PropertyManager findByEmail(String email);

}

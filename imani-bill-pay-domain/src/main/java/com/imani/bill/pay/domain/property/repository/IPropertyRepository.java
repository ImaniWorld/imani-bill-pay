package com.imani.bill.pay.domain.property.repository;

import com.imani.bill.pay.domain.property.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author manyce400
 */
@Repository
public interface IPropertyRepository extends JpaRepository<Property, Long> {


    @Query("Select property From Property property JOIN FETCH property.floors Where property.id =?1")
    public Property findUniquePropertyFetchFloors(Long id);

}

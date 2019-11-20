package com.imani.bill.pay.domain.property.repository;

import com.imani.bill.pay.domain.geographical.Borough;
import com.imani.bill.pay.domain.geographical.City;
import com.imani.bill.pay.domain.property.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author manyce400
 */
@Repository
public interface IPropertyRepository extends JpaRepository<Property, Long> {


    @Query("Select property From Property property JOIN FETCH property.floors Where property.id =?1")
    public Property findUniquePropertyFetchFloors(Long id);

    @Query("Select property From Property property Where property.borough =?1")
    public List<Property> findBoroughProperties(Borough borough);

    @Query("Select property From Property property JOIN FETCH property.floors Where property.borough =?1")
    public List<Property> findBoroughPropertiesFetchFloors(Borough borough);

    @Query("Select property From Property property Where property.propertyNumber =?1 and property.streetName =?2 and property.zipCode =?3")
    public List<Property> findUniqueProperties(String propertyNumber, String streetName, String zipCode);

    @Query("Select property From Property property Where property.propertyNumber =?1 and property.streetName =?2 and property.zipCode =?3 and property.borough =?4")
    public List<Property> findUniqueProperties(String propertyNumber, String streetName, String zipCode, Borough borough);

    @Query("Select property From Property property Where property.propertyNumber =?1 and property.streetName =?2 and property.zipCode =?3 and property.city =?4")
    public List<Property> findUniqueProperties(String propertyNumber, String streetName, String zipCode, City city);

}

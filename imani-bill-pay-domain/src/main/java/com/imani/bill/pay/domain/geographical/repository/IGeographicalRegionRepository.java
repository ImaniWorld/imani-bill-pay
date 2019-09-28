package com.imani.bill.pay.domain.geographical.repository;

import com.imani.bill.pay.domain.geographical.GeographicalRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author manyce400
 */
@Repository
public interface IGeographicalRegionRepository extends JpaRepository<GeographicalRegion, Long> {


    @Query("Select  geographicalRegion From GeographicalRegion geographicalRegion Where geographicalRegion.regionCode =?1")
    public GeographicalRegion findGeographicalRegionByCode(String regionCode);

}

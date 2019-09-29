package com.imani.bill.pay.service.geographical;

import com.imani.bill.pay.domain.geographical.GeographicalRegion;
import org.springframework.stereotype.Repository;

/**
 * @author manyce400
 */
@Repository
public interface IGeographicalRegionService {

    public GeographicalRegion findGeographicalRegionByCode(String regionCode);

}

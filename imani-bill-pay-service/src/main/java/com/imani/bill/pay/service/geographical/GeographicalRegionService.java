package com.imani.bill.pay.service.geographical;

import com.imani.bill.pay.domain.geographical.GeographicalRegion;
import com.imani.bill.pay.domain.geographical.repository.IGeographicalRegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;

/**
 * @author manyce400
 */
@Service(GeographicalRegionService.SPRING_BEAN)
public class GeographicalRegionService implements IGeographicalRegionService {



    @Autowired
    private IGeographicalRegionRepository iGeographicalRegionRepository;



    public static final String SPRING_BEAN = "com.imani.cash.domain.service.geographical.GeographicalRegionService";


    @Override
    public GeographicalRegion findGeographicalRegionByCode(String regionCode) {
        Assert.notNull(regionCode, "regionCode cannot be null");
        GeographicalRegion geographicalRegion = iGeographicalRegionRepository.findGeographicalRegionByCode(regionCode);
        return geographicalRegion;
    }


    @PostConstruct
    public void runPostConstruct() {
        System.out.println("\n\nRunning Post Construct\n\n");
        GeographicalRegion geographicalRegion = iGeographicalRegionRepository.findGeographicalRegionByCode("NA");
        System.out.println("geographicalRegion = " + geographicalRegion);
        System.out.println("\n\nCompleted Post Construct\n\n");
    }

}

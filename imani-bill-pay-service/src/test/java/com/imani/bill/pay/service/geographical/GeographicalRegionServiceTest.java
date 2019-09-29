package com.imani.bill.pay.service.geographical;

import com.imani.bill.pay.domain.geographical.GeographicalRegion;
import com.imani.bill.pay.domain.geographical.repository.IGeographicalRegionRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author manyce400
 */
@RunWith(MockitoJUnitRunner.class)
public class GeographicalRegionServiceTest {



    @Mock
    private IGeographicalRegionRepository iGeographicalRegionRepository;

    @InjectMocks
    private GeographicalRegionService geographicalRegionService;

    private GeographicalRegion geographicalRegion;

    @Before
    public void beforeTest() {
        geographicalRegion = new GeographicalRegion();
        geographicalRegion.setRegionCode("USA");
        geographicalRegion.setRegionName("United States of America");
    }
    
    @Test
    public void testFindGeographicalRegionByCode() {
        Mockito.when(iGeographicalRegionRepository.findGeographicalRegionByCode(Mockito.anyString())).thenReturn(geographicalRegion);
        GeographicalRegion result = geographicalRegionService.findGeographicalRegionByCode("USA");
        Assert.assertEquals(geographicalRegion, result);
    }

}

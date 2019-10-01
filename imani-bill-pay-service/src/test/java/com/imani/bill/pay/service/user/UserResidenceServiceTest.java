package com.imani.bill.pay.service.user;

import com.imani.bill.pay.domain.user.repository.IUserResidenceRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author manyce400
 */
@RunWith(MockitoJUnitRunner.class)
public class UserResidenceServiceTest {


    @Mock
    private IUserResidenceRepository iUserResidenceRepository;

    @InjectMocks
    private UserResidenceService userResidenceService;


    @Test
    public void testBuildUserResidence() {

    }
}

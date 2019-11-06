package com.imani.bill.pay.service.user;

import com.imani.bill.pay.domain.contact.EmbeddedContactInfo;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.repository.IUserRecordRepository;
import com.imani.bill.pay.service.mock.IMockUserRecordTestBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;

/**
 * @author manyce400 
 */
@RunWith(MockitoJUnitRunner.class)
public class UserRecordAuthServiceTest implements IMockUserRecordTestBuilder {



    private UserRecord userRecord;

    private EmbeddedContactInfo embeddedContactInfo;
    
    @Mock
    private IUserRecordRepository iUserRecordRepository;
    
    @Mock
    private AuthenticationManager authenticationManager;
    
    @InjectMocks
    private UserRecordAuthService userRecordAuthService;


    
    @Before
    public void beforeTest() {
        embeddedContactInfo = EmbeddedContactInfo.builder()
                .email("testuser@imanicash.com")
                .build();

        userRecord = UserRecord.builder()
                .embeddedContactInfo(embeddedContactInfo)
                .firstName("Test")
                .lastName("User")
                .accountLocked(false)
                .unsuccessfulLoginAttempts(0)
                .build();

        // Mock out call to load user from DB
        Mockito.when(iUserRecordRepository.findByUserEmail("testuser@imanicash.com")).thenReturn(userRecord);
    }
    
    
    @Test
    public void testTrackUnsuccessfulLoginAttempts() {
        Integer unsuccessfulAttempts = userRecordAuthService.trackUnsuccessfulLoginAttempts(userRecord);
        Assert.assertEquals(new Integer(1), unsuccessfulAttempts);
        Assert.assertFalse(userRecord.isAccountLocked());

        unsuccessfulAttempts = userRecordAuthService.trackUnsuccessfulLoginAttempts(userRecord);
        Assert.assertEquals(new Integer(2), unsuccessfulAttempts);
        Assert.assertFalse(userRecord.isAccountLocked());

        unsuccessfulAttempts = userRecordAuthService.trackUnsuccessfulLoginAttempts(userRecord);
        Assert.assertEquals(new Integer(3), unsuccessfulAttempts);
        Assert.assertFalse(userRecord.isAccountLocked());

        // After 3rd time verify that the UserRecord is now locked.
        unsuccessfulAttempts = userRecordAuthService.trackUnsuccessfulLoginAttempts(userRecord);
        Assert.assertEquals(new Integer(3), unsuccessfulAttempts);
        Assert.assertTrue(userRecord.isAccountLocked());
    }

//    @Test
//    public void testExecuteUserRecordLogin() {
//        APIGatewayEvent<UserRecordRequest, GenericAPIGatewayResponse> apiGatewayEventResponse = userRecordAuthService.executeUserRecordLogin(userRecord);
//        Assert.assertEquals(APIGatewayEventStatusE.Success, apiGatewayEventResponse.getResponseBody().get().getApiGatewayEventStatusE());
//        Assert.assertTrue(apiGatewayEventResponse.getEventUserRecord().get().isLoggedIn());
//        Assert.assertFalse(apiGatewayEventResponse.getEventUserRecord().get().isAccountLocked());
//        Assert.assertFalse(apiGatewayEventResponse.getEventUserRecord().get().isResetPassword());
//        Assert.assertEquals(userRecord, apiGatewayEventResponse.getEventUserRecord().get());
//        Assert.assertEquals(new Integer(0), apiGatewayEventResponse.getEventUserRecord().get().getUnsuccessfulLoginAttempts());
//        Assert.assertNull(apiGatewayEventResponse.getEventUserRecord().get().getPassword()); // Verify that password has been set to null for API response, we cant let this leak outside.
//    }

//    @Test
//    public void testGetBadCredentialsUserRecordAuthentication() {
//        UserRecordRequest userRecordAuth  = userRecordAuthService.getBadCredentialsUserRecordAuthentication(userRecord, 1);
//        Assert.assertFalse(userRecordAuth.getUserRecord().isLoggedIn());
//        Assert.assertFalse(userRecordAuth.getUserRecord().isAccountLocked());
//        Assert.assertEquals(userRecord, userRecordAuth.getUserRecord());
//    }
    
}

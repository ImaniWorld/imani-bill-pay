package com.imani.bill.pay.service.user;

import com.imani.bill.pay.domain.contact.EmbeddedContactInfo;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.UserRecordAuth;
import com.imani.bill.pay.domain.user.repository.IUserRecordRepository;
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
public class UserRecordAuthServiceTest {



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

    @Test
    public void testExecuteUserRecordLogin() {
        UserRecordAuth userRecordAuth = userRecordAuthService.executeUserRecordLogin(userRecord);
        Assert.assertTrue(userRecordAuth.getUserRecord().isLoggedIn());
        Assert.assertFalse(userRecordAuth.getUserRecord().isAccountLocked());
        Assert.assertFalse(userRecordAuth.getUserRecord().isResetPassword());
        Assert.assertEquals(userRecord, userRecordAuth.getUserRecord());
        Assert.assertEquals(new Integer(0), userRecordAuth.getUserRecord().getUnsuccessfulLoginAttempts());
    }

    @Test
    public void testGetBadCredentialsUserRecordAuthentication() {
        UserRecordAuth userRecordAuth  = userRecordAuthService.getBadCredentialsUserRecordAuthentication(userRecord, 1);
        Assert.assertFalse(userRecordAuth.getUserRecord().isLoggedIn());
        Assert.assertFalse(userRecordAuth.getUserRecord().isAccountLocked());
        Assert.assertEquals(userRecord, userRecordAuth.getUserRecord());
    }
    
}

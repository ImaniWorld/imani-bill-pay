package com.imani.bill.pay.service.user;

import com.imani.bill.pay.domain.contact.EmbeddedContactInfo;
import com.imani.bill.pay.domain.gateway.APIGatewayEventStatusE;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.UserRecordEvent;
import com.imani.bill.pay.domain.user.repository.IUserRecordRepository;
import com.imani.bill.pay.service.encryption.IClearTextEncryptionService;
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
public class UserRecordManagerServiceTest {



    private UserRecord userRecord;

    private EmbeddedContactInfo embeddedContactInfo;


    @Mock
    private IUserRecordRepository iUserRecordRepository;

    @Mock
    private IClearTextEncryptionService iClearTextEncryptionService;

    @InjectMocks
    private UserRecordManagerService userRecordManagerService;




    @Before
    public void beforeTest() {
        embeddedContactInfo = EmbeddedContactInfo.builder()
                .email("testuser@imanicash.com")
                .build();

        userRecord = UserRecord.builder()
                .embeddedContactInfo(embeddedContactInfo)
                .firstName("Test")
                .lastName("User")
                .password("boss")
                .accountLocked(false)
                .unsuccessfulLoginAttempts(0)
                .build();
    }



    @Test
    public void testGetUserRecordEventOnSucess() {
        UserRecordEvent userRecordEvent = userRecordManagerService.getUserRecordEventOnSucess(userRecord);
        Assert.assertEquals(APIGatewayEventStatusE.Success, userRecordEvent.getApiGatewayEventStatusE());
        Assert.assertEquals(userRecord, userRecordEvent.getUserRecord());
    }

    @Test
    public void testGetUserRecordEventOnInvalidUser() {
        UserRecordEvent userRecordEvent = userRecordManagerService.getUserRecordEventOnInvalidUser(userRecord);
        Assert.assertEquals(APIGatewayEventStatusE.InvalidRequest, userRecordEvent.getApiGatewayEventStatusE());
        Assert.assertEquals(userRecord, userRecordEvent.getUserRecord());
    }


    @Test
    public void testRegisterUserRecordSuccessfull() {
        // This is simulating registering a new user when the UserRecord details passed does not exist
        Mockito.when(iClearTextEncryptionService.encryptClearText(Mockito.any())).thenReturn("$Hllkskskdll848433");
        UserRecordEvent userRecordEvent = userRecordManagerService.registerUserRecord(userRecord);
        Assert.assertEquals(APIGatewayEventStatusE.Success, userRecordEvent.getApiGatewayEventStatusE());
        Assert.assertEquals(userRecord, userRecordEvent.getUserRecord());
    }

    @Test
    public void testRegisterUserRecordCouldNotCreateUser() {
        // This is simulating registering a UserRecord which already exists
        Mockito.when(iUserRecordRepository.findByUserEmailAndMobilePhone(Mockito.any(), Mockito.any())).thenReturn(userRecord);
        Mockito.when(iClearTextEncryptionService.encryptClearText(Mockito.any())).thenReturn("$Hllkskskdll848433");
        UserRecordEvent userRecordEvent = userRecordManagerService.registerUserRecord(userRecord);
        Assert.assertEquals(APIGatewayEventStatusE.InvalidRequest, userRecordEvent.getApiGatewayEventStatusE());
        Assert.assertEquals(userRecord, userRecordEvent.getUserRecord());
    }

}

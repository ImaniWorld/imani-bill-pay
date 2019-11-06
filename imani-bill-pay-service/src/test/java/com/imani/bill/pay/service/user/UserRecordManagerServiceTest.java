package com.imani.bill.pay.service.user;

import com.imani.bill.pay.domain.contact.EmbeddedContactInfo;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.repository.IUserRecordRepository;
import com.imani.bill.pay.service.encryption.IClearTextEncryptionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
        //UserRecordRequest userRecordRequest = userRecordManagerService.getUserRecordEventOnSucess(userRecord);
        //Assert.assertEquals(APIGatewayEventStatusE.Success, userRecordRequest.getApiGatewayEventStatusE());
        //Assert.assertEquals(userRecord, userRecordRequest.getUserRecord());
    }
//
//    @Test
//    public void testGetUserRecordEventOnInvalidUser() {
//        UserRecordRequest userRecordRequest = userRecordManagerService.getUserRecordEventOnInvalidUser(userRecord);
//        //Assert.assertEquals(APIGatewayEventStatusE.InvalidRequest, userRecordRequest.getApiGatewayEventStatusE());
//        //Assert.assertEquals(userRecord, userRecordRequest.getUserRecord());
//    }


//    @Test
//    public void testRegisterUserRecordSuccessfull() {
//        // This is simulating registering a new user when the UserRecord details passed does not exist
//        Mockito.when(iClearTextEncryptionService.encryptClearText(Mockito.any())).thenReturn("$Hllkskskdll848433");
//        UserRecordRequest userRecordRequest = userRecordManagerService.registerUserRecord(userRecord);
//        //Assert.assertEquals(APIGatewayEventStatusE.Success, userRecordRequest.getApiGatewayEventStatusE());
//        //Assert.assertEquals(userRecord, userRecordRequest.getUserRecord());
//    }
//
//    @Test
//    public void testRegisterUserRecordCouldNotCreateUser() {
//        // This is simulating registering a UserRecord which already exists
//        Mockito.when(iUserRecordRepository.findByUserEmailAndMobilePhone(Mockito.any(), Mockito.any())).thenReturn(userRecord);
//        Mockito.when(iClearTextEncryptionService.encryptClearText(Mockito.any())).thenReturn("$Hllkskskdll848433");
//        UserRecordRequest userRecordRequest = userRecordManagerService.registerUserRecord(userRecord);
//        //Assert.assertEquals(APIGatewayEventStatusE.InvalidRequest, userRecordRequest.getApiGatewayEventStatusE());
//        //Assert.assertEquals(userRecord, userRecordRequest.getUserRecord());
//    }

}

package com.imani.bill.pay.service.encryption;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

/**
 * @author manyce400
 */
@Service(ClearTextEncryptionService.SPRING_BEAN)
public class ClearTextEncryptionService implements IClearTextEncryptionService {


    public static final String SPRING_BEAN = "com.imani.bill.pay.service.encryption.ClearTextEncryptionService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ClearTextEncryptionService.class);

    @Override
    public String encryptClearText(String clearText) {
        LOGGER.debug("Attempting to encrypt clearText.....");
        String encoded = BCrypt.hashpw(clearText.toString(), BCrypt.gensalt(4));
        return encoded;
    }

    @Override
    public boolean matchesEncryptedValue(String clearText, String encryptedText) {
        LOGGER.debug("Checking to see if clear text matches encrpted value....");
        return BCrypt.checkpw(clearText, encryptedText);
    }

//    public static void main(String[] args) {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        String result = encoder.encode("BillPayNodeClient8909!#");
//        System.out.println("result = " + result);
//    }

}

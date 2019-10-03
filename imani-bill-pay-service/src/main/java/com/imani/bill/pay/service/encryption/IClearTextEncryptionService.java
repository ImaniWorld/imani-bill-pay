package com.imani.bill.pay.service.encryption;

/**
 * @author manyce400
 */
public interface IClearTextEncryptionService {

    /**
     * Performs a one way only clear text encryption strategy.  Note that one way encryption cannot be reversed.
     *
     * @param clearText
     * @return Encrypted one way String
     */
    public String encryptClearText(String clearText);


    /**
     * Takes clear text and checks to see after hashing that it matches the encrypted text passed as argument.
     *
     * @param clearText
     * @param encryptedText
     * @return <code>true</code> IF a match is found
     */
    public boolean matchesEncryptedValue(String clearText, String encryptedText);

}

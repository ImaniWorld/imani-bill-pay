package com.imani.bill.pay;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author manyce400
 */
@Configuration
public class JasyptConfiguration {

    public static final String JACSKON_OBJECT_MAPPER = "com.imani.bill.pay.JasyptConfiguration";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(JasyptConfiguration.class);

    @Bean(name = JACSKON_OBJECT_MAPPER)
    public StringEncryptor stringEncryptor() {
        LOGGER.info("Configuring SpringBoot with Jasypt encryption capabilities...");
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword("HolaHovito#!");
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
//        String encryptedValue = encryptor.encrypt("sk_test_Yk6COIu8mI0GH5ttBzV6Ubu800txYLbF2A");
//        System.out.println("\n\n\nencryptedValue = " + encryptedValue + "\n\n\n");
        return encryptor;
    }

}

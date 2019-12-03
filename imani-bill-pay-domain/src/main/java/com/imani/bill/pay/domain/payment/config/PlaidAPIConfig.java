package com.imani.bill.pay.domain.payment.config;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Configuration Bean for Plaid API.
 *
 * @author manyce400
 */
@Configuration
@PropertySource({
        "classpath:plaid-gateway-${spring.profiles.active}.properties"
})
public class PlaidAPIConfig {


    @Value("${plaid.gateway.client.id}")
    private String clientID;

    @Value("${plaid.gateway.public.key}")
    private String publicKey;



    public PlaidAPIConfig() {

    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("clientID", clientID)
                .append("publicKey", publicKey)
                .toString();
    }
}

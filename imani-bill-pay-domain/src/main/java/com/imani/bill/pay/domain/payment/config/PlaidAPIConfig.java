package com.imani.bill.pay.domain.payment.config;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.Assert;

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

    @Value("${plaid.gateway.secret}")
    private String secret;

    @Value("${plaid.gateway.api.base.url}")
    private String plaidAPIURL;



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

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getPlaidAPIURL() {
        return plaidAPIURL;
    }

    public void setPlaidAPIURL(String plaidAPIURL) {
        this.plaidAPIURL = plaidAPIURL;
    }

    public String getAPIPathURL(String path) {
        Assert.notNull(path, "path cannot be null");
        String apiPathURL = new StringBuilder(plaidAPIURL)
                .append(path)
                .toString();
        return apiPathURL;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("clientID", clientID)
                .append("publicKey", publicKey)
                .append("secret", secret)
                .append("plaidAPIURL", plaidAPIURL)
                .toString();
    }

}

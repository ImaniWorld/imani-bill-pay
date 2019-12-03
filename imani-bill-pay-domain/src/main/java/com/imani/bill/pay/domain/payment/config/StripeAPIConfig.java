package com.imani.bill.pay.domain.payment.config;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Configuration Bean for Stripe API.
 *
 * @author manyce400
 */
@Configuration
@PropertySource({
        "classpath:stripe-gateway-${spring.profiles.active}.properties"
})
public class StripeAPIConfig {


    @Value("${stripe.gateway.public.key}")
    private String publicKey;

    @Value("${stripe.gateway.api.key}")
    private String apiKey;

    public StripeAPIConfig() {

    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("publicKey", publicKey)
                .append("apiKey", apiKey)
                .toString();
    }
}
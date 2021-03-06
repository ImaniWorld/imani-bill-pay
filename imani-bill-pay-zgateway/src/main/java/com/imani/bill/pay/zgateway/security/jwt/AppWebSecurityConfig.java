package com.imani.bill.pay.zgateway.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppWebSecurityConfig extends WebSecurityConfigurerAdapter {



    @Autowired
    JWTTokenProvider jwtTokenProvider;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/auth/api/jwt/validate").permitAll() // EndPoint to validate and request application JWT token
                .antMatchers("/billpay/inquiry/new").permitAll() // EndPoint for potential customers to inquire about BillPay
                .antMatchers("/auth/user/login").permitAll()
                .antMatchers("/auth/user/logout").permitAll()
                .antMatchers("/stripe/plaid/integration").permitAll() // EndPoint for Plaid and Stripe Integration
                .antMatchers("/register/plaid/account/**/**").permitAll() // EndPoint for Plaid and Stripe Integration
                .anyRequest().authenticated()
                .and()
                .apply(new JWTConfigurer(jwtTokenProvider));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Ensures that all password verifications done through this API uses BCrypt encryption
        return new BCryptPasswordEncoder();
    }

}

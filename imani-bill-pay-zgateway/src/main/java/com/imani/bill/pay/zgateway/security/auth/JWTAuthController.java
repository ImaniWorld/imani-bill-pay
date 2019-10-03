package com.imani.bill.pay.zgateway.security.auth;

import com.google.common.collect.ImmutableList;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.service.user.JWTUserDetailsService;
import com.imani.bill.pay.zgateway.security.jwt.JWTTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author manyce400
 */
@RestController
@RequestMapping("/auth/api/jwt")
public class JWTAuthController {


    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    @Qualifier(JWTUserDetailsService.SPRING_BEAN)
    private UserDetailsService userDetailsService;


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(JWTAuthController.class);




    @PostMapping("/validate")
    public ResponseEntity validateJWTSigninRequest(@RequestBody UserRecord userRecord) {
        LOGGER.info("Executing JWT signin request for user:=> {}", userRecord);

        try {
            String username = userRecord.getEmbeddedContactInfo().getEmail();

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            LOGGER.info("UserDetails found=> {}", userDetails);

            List<String> roles = ImmutableList.of("APIUser");
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRecord.getEmbeddedContactInfo().getEmail(), userRecord.getPassword()));
            System.out.println("auth = " + auth);
            String token = jwtTokenProvider.createToken(username, roles);

            Map<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("token", token);
            return ResponseEntity.ok(model);
        } catch (Exception e) {
            LOGGER.error("Exception was thrown", e);
            throw new BadCredentialsException("Invalid username/password supplied");
        }

    }



}

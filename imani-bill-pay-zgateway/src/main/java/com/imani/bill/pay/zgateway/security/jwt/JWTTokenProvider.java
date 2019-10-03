package com.imani.bill.pay.zgateway.security.jwt;

import com.google.common.collect.ImmutableList;
import com.imani.bill.pay.service.user.JWTUserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author manyce400
 */
@Component
public class JWTTokenProvider {




    @Value("${security.jwt.token.expire-length:3600000}")
    private long validityInMilliseconds = TimeUnit.HOURS.toHours(3); // 1h

    @Autowired
    @Qualifier(JWTUserDetailsService.SPRING_BEAN)
    private UserDetailsService userDetailsService;

    SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);


    public String createToken(String username, List<String> roles) throws UnsupportedEncodingException {

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles);

        // Create a secret key to be used

        // Build JWT Token expiration properties
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()//
                .setClaims(claims)//
                .setIssuedAt(now)//
                .setExpiration(validity)//
                .signWith(secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Expired or invalid JWT token");
        }
    }


    public static void main(String[] args) {
        JWTTokenProvider jwtTokenProvider = new JWTTokenProvider();
        List<String> s = ImmutableList.of("Help");
        try {

            SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
            String base64Key = Encoders.BASE64.encode(secretKey.getEncoded());
            System.out.println("base64Key = " + base64Key);

//            String token = jwtTokenProvider.createToken("Hello", s);
//            System.out.println("token = " + token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

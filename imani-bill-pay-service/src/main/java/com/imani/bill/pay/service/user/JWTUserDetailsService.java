package com.imani.bill.pay.service.user;


import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.repository.IUserRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Functionality to retrieve and verify API User login credentials as part of generating JWT Token.
 *
 * @author manyce400
 */
@Service(JWTUserDetailsService.SPRING_BEAN)
public class JWTUserDetailsService implements UserDetailsService {



    @Autowired
    private IUserRecordRepository iUserRecordRepository;


    public static final String SPRING_BEAN = "com.imani.bill.pay.service.user.JWTUserDetailsService";


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(JWTUserDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        LOGGER.debug("Attempting to find UserRecord with email:=> {}", userName);
        UserRecord userRecord = iUserRecordRepository.findByUserEmail(userName);
        LOGGER.debug("UserRecord found: {}", userRecord);

        // This implementation will only allow APIUser to login
        if(userRecord != null) {
            JWTUserDetails userDetails = new JWTUserDetails(userRecord);
            return userDetails;
        }

        throw new UsernameNotFoundException("Provided user name cannot be found in the system");
    }
}

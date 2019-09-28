package com.imani.bill.pay.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

/**
 * Configuration will enable JPA Auditing.  All classes that extend AuditableRecord will automatically
 * get auditing details automatically populated.
 *
 * @author manyce400
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfiguration {


    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }

    /**
     * Default implementation will return no user
     */
    private static final class AuditorAwareImpl implements AuditorAware<String> {
        @Override
        public Optional<String> getCurrentAuditor() {
            return Optional.empty();
        }
    }

}

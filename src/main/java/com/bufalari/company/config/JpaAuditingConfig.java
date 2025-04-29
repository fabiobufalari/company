package com.bufalari.company.config;

import com.bufalari.company.auditing.AuditorAwareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProviderCompany") // Ref único
public class JpaAuditingConfig {

    @Bean
    public AuditorAware<String> auditorProviderCompany() { // Nome do bean corresponde
        return new AuditorAwareImpl();
    }
}
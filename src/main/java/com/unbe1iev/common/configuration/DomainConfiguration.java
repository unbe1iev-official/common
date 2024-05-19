package com.unbe1iev.common.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

@Configuration
public class DomainConfiguration {

    @Bean
    @RequestScope
    public DomainHolder domainHolder() {
        return new DomainHolder();
    }
}

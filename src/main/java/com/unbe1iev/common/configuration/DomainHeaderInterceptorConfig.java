package com.unbe1iev.common.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class DomainHeaderInterceptorConfig implements WebMvcConfigurer {

    private final DomainHolder domainHolder;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(domainInterceptor());
    }

    @Bean
    public DomainInterceptor domainInterceptor() {
        return new DomainInterceptor(domainHolder);
    }
}

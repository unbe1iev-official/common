package com.unbe1iev.common.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.util.Assert;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static com.unbe1iev.common.util.SecurityUtil.ADMIN_ROLE;
import static com.unbe1iev.common.util.SecurityUtil.CREATOR_ROLE;

@Configuration
@EnableWebSecurity
@Slf4j
public class OAuth2ResourceServerSecurityConfiguration {

    private final String[] permitAllMatchers;
    private final List<String> trustedIssuers;

    public OAuth2ResourceServerSecurityConfiguration(
            @Value("${security.permit.all.matchers:}") String[] permitAllMatchers,
            @Value("${security.trusted.issuers:}") String[] trustedIssuers) {

        log.info("additional matchers {}", (Object) permitAllMatchers);
        this.permitAllMatchers = permitAllMatchers;

        Assert.notEmpty(trustedIssuers, "trustedIssuers cannot be empty");
        this.trustedIssuers = Arrays.asList(trustedIssuers);
        log.info("trustedIssuers {}", this.trustedIssuers);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.NEVER))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui*/**", "/actuator/**")
                        .permitAll()
                        .requestMatchers(permitAllMatchers)
                        .permitAll()
                        .requestMatchers("/api/**")
                        .hasAnyRole(CREATOR_ROLE, ADMIN_ROLE)
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(new CustomJwtAuthenticationConverter())
                        )
                        .authenticationManagerResolver(
                                new JwtIssuerAuthenticationManagerResolver(
                                        new CustomTrustedIssuerJwtAuthenticationManagerResolver(
                                                trustedIssuers::contains))));
        return http.build();
    }

    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(""); // Remove the ROLE_ prefix
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:4400",
                "https://unbe1iev.com", "https://sso.unbe1iev.com/"));
        configuration.setAllowedMethods(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

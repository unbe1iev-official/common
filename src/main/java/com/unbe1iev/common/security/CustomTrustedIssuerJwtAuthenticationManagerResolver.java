package com.unbe1iev.common.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

@Slf4j
public class CustomTrustedIssuerJwtAuthenticationManagerResolver implements AuthenticationManagerResolver<String> {

    private final Map<String, AuthenticationManager> authenticationManagers = new ConcurrentHashMap<>();

    private final Predicate<String> trustedIssuer;

    CustomTrustedIssuerJwtAuthenticationManagerResolver(Predicate<String> trustedIssuer) {
        this.trustedIssuer = trustedIssuer;
    }

    @Override
    public AuthenticationManager resolve(String issuer) {
        if (this.trustedIssuer.test(issuer)) {
            AuthenticationManager authenticationManager = this.authenticationManagers.computeIfAbsent(issuer,
                    _ -> {
                        log.debug("Constructing AuthenticationManager");
                        JwtDecoder jwtDecoder = JwtDecoders.fromIssuerLocation(issuer);
                        JwtAuthenticationProvider provider = new JwtAuthenticationProvider(jwtDecoder);
                        provider.setJwtAuthenticationConverter(new CustomJwtAuthenticationConverter());
                        return provider::authenticate;
                    });
            log.debug("Resolved AuthenticationManager for issuer '{}'", issuer);
            return authenticationManager;
        } else {
            log.debug("Did not resolve AuthenticationManager since issuer is not trusted");
        }
        return null;
    }
}
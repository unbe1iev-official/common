package com.unbe1iev.common.security;

import com.unbe1iev.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
public class CustomJwtAuthenticationConverter extends JwtAuthenticationConverter {

    public CustomJwtAuthenticationConverter() {
        setJwtGrantedAuthoritiesConverter(new CustomGrantedAuthoritiesConverter());
    }

    private static class CustomGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
        @Override
        public Collection<GrantedAuthority> convert(Jwt jwt) {
            Collection<String> authorities = SecurityUtil.getAuthorities(jwt.getClaims());
            log.info("Extracted authorities from JWT: {}", authorities);
            return authorities.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
        }
    }
}
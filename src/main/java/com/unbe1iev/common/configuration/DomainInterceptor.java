package com.unbe1iev.common.configuration;

import com.unbe1iev.common.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Objects;

import static com.unbe1iev.common.util.SecurityUtil.X_DOMAIN_HEADER;

@Slf4j
@RequiredArgsConstructor
public class DomainInterceptor implements HandlerInterceptor {

    private final DomainHolder domainHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        String domain = request.getHeader(X_DOMAIN_HEADER);
        String userDomain = SecurityUtil.getDomain();

        log.info("domain {}, userdomain {} ", domain, userDomain);
        if (domain != null && userDomain != null && !Objects.equals(domain, userDomain)) {
            throw new AccessDeniedException("Domains not match");
        }
        domainHolder.setDomain(domain);
        return true;
    }
}
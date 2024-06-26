package com.unbe1iev.common.configuration;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Aspect
@Component
@ConditionalOnProperty(name = "domain.required-checker", havingValue = "enabled")
@RequiredArgsConstructor
public class DomainCheckerAspect {

    private final DomainHolder domainHolder;
    private final DomainCheckerProperties domainCheckerProperties;

    private static final String REQUIRED_DOMAIN = "unbe1iev";
    private List<String> combinedNotRequired = new ArrayList<>();

    @Value("${domain.local-not-required:}")
    private List<String> localNotRequired;

    @PostConstruct
    public void mergeConfigurations() {
        combinedNotRequired.addAll(domainCheckerProperties.getNotRequired());
        combinedNotRequired.addAll(localNotRequired);
    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void beanAnnotatedWithRestController() {
        // pointcut definition
    }

    @Around("beanAnnotatedWithRestController()")
    public Object classMarkedWithRestController(ProceedingJoinPoint joinPoint) throws Throwable {
        throwIfNotInDomainContext(joinPoint.getSignature());
        return joinPoint.proceed();
    }

    private void throwIfNotInDomainContext(Signature signature) {
        String domain = domainHolder.getDomain();
        if (domainIsRequired(signature) && (!StringUtils.hasText(domain) || !REQUIRED_DOMAIN.equals(domain))) {
            throw new AccessDeniedException("Domain is required and must match the predefined domain!");
        }
    }

    private boolean domainIsRequired(Signature signature) {
        String key = String.format("%s.%s", signature.getDeclaringType().getSimpleName(), signature.getName());
        return domainCheckerProperties.getNotRequired().stream()
                .noneMatch(method -> method.equals(key));
    }

    @PostConstruct
    void printInfo() {
        log.info("DomainChecker aspect is enabled");
    }
}


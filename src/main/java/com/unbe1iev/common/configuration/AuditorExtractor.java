package com.unbe1iev.common.configuration;

import com.unbe1iev.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;

import java.util.Optional;

@Slf4j
@Component
public class AuditorExtractor implements AuditorAware<String> {

    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {
        return Optional.of(SecurityUtil.getLogin());
    }
}
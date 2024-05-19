package com.unbe1iev.common.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Component
@ConfigurationProperties(prefix = "domain")
public class DomainCheckerProperties {

    private List<String> notRequired = new ArrayList<>();
    private String requiredChecker;  // enabled by default
}
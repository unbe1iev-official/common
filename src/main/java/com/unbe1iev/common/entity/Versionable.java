package com.unbe1iev.common.entity;

import java.time.LocalDateTime;

public interface Versionable<T> {

    T getId();

    Long getVersion();

    LocalDateTime getLastModifiedDate();

    String getLastModifiedBy();
}

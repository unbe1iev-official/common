package com.unbe1iev.common.configuration;

import com.unbe1iev.common.entity.Auditable;
import com.unbe1iev.common.util.SecurityUtil;
import org.springframework.stereotype.Component;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.ZonedDateTime;

@Component
public class CustomAuditingEntityListener {

    @PrePersist
    public void prePersist(Auditable auditable) {
        auditable.setCreatedDateTime(ZonedDateTime.now());
        auditable.setLastModifiedDateTime(ZonedDateTime.now());
        auditable.setVersion(0L);

        String currentUser = SecurityUtil.getLoggedUserKeycloakId();
        auditable.setCreatedBy(currentUser);
        auditable.setLastModifiedBy(currentUser);
    }

    @PreUpdate
    public void preUpdate(Auditable auditable) {
        String currentUser = SecurityUtil.getLoggedUserKeycloakId();
        auditable.setLastModifiedDateTime(ZonedDateTime.now());
        auditable.setLastModifiedBy(currentUser);
        auditable.setVersion(auditable.getVersion() + 1L);
    }
}

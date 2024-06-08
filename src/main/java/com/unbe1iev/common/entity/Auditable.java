package com.unbe1iev.common.entity;

import com.unbe1iev.common.configuration.CustomAuditingEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(CustomAuditingEntityListener.class)
public abstract class Auditable {

    @NotNull
    private Long version;

    @NotNull
    private ZonedDateTime createdDateTime;

    @NotNull
    private String createdBy;

    @NotNull
    private ZonedDateTime lastModifiedDateTime;

    @NotNull
    private String lastModifiedBy;
}

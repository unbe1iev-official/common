package com.unbe1iev.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {

    @NotNull
    private Long version;

    @NotNull
    @CreatedDate
    private LocalDateTime createdDateTime;

    @NotNull
    @CreatedBy
    private String createdBy;

    @NotNull
    @LastModifiedDate
    private LocalDateTime lastModifiedDateTime;

    @NotNull
    @LastModifiedBy
    private String lastModifiedBy;
}
package com.fawry.MoviesApp.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class AuditEntity {

    @Column(name = "created_by_id",updatable = false)
    private long createdById;

    @Column(name = "created_by",updatable = false)
    private String createdBy;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "last_modified_id")
    private long lastModifiedById;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_at")
    private Date lastModifiedAt;

    @Column(name = "deleted_by_id")
    private long deletedById;

    @Column(name = "deleted_by")
    private String deletedBy;

    @Column(name = "deleted_at")
    private Date deletedAt;

}

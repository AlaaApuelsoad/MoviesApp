package com.alaa.MoviesApp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuditEntity {

    @Column(name = "created_by_id", updatable = false)
    private Long createdById;

    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Column(name = "last_modified_id")
    private Long lastModifiedById;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastModifiedAt;

    @Column(name = "deleted_by_id")
    private Long deletedById = null;

    @Column(name = "deleted_by")
    private String deletedBy;

    @Column(name = "deleted_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime deletedAt;

    @PrePersist
    private void onCreate() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() &&
                authentication.getPrincipal() instanceof UserDetails userDetails) {
            this.createdBy = getCurrentUsername(userDetails);
            this.createdById = getCurrentUserId(userDetails);
        } else {
            this.createdBy = "SYSTEM";
            this.createdById = 0L;
        }

        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    private void onPreUpdate() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() &&
                authentication.getPrincipal() instanceof UserDetails userDetails) {
            this.lastModifiedBy = getCurrentUsername(userDetails);
            this.lastModifiedById = getCurrentUserId(userDetails);
        } else {
            this.lastModifiedBy = "SYSTEM";
            this.lastModifiedById = 0L;
        }
        this.lastModifiedAt = LocalDateTime.now();
    }

    private Long getCurrentUserId(UserDetails userDetails) {
        if (userDetails instanceof User) {
            return ((User) userDetails).getId();
        }
        return 0L;
    }
    private String getCurrentUsername(UserDetails userDetails){
        if (userDetails instanceof User) {
            return userDetails.getUsername();
        }
        return "SYSTEM";
    }

}

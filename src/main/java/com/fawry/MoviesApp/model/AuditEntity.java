package com.fawry.MoviesApp.model;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Column(name = "last_modified_id")
    private Long lastModifiedById;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_at")
    private LocalDateTime lastModifiedAt;

    @Column(name = "deleted_by_id")
    private Long deletedById = null;

    @Column(name = "deleted_by")
    private String deletedBy;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PrePersist
    private void onCreate() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            this.createdBy = userDetails.getUsername();
            this.createdById = getUserIdFromUserDetails(userDetails);
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
            this.lastModifiedBy = userDetails.getUsername();
            this.lastModifiedById = getUserIdFromUserDetails(userDetails);
        } else {
            this.lastModifiedBy = "SYSTEM";
            this.lastModifiedById = 0L;
        }
        this.lastModifiedAt = LocalDateTime.now();
    }

    @PreRemove
    private void onPreRemove() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() &&
                authentication.getPrincipal() instanceof UserDetails userDetails) {
            this.deletedBy = userDetails.getUsername();
            this.deletedById = getUserIdFromUserDetails(userDetails);
        } else {
            this.deletedBy = "SYSTEM";
            this.deletedById = 0L;
        }
        this.deletedAt = LocalDateTime.now();
    }

    private Long getUserIdFromUserDetails(UserDetails userDetails) {
        if (userDetails instanceof User) {
            return ((User) userDetails).getId();
        }
        throw new IllegalStateException("User ID not found in UserDetails implementation");
    }

}

package com.sparta.temueats.global;

import com.sparta.temueats.global.ex.CustomApiException;
import com.sparta.temueats.security.UserDetailsImpl;
import com.sparta.temueats.user.entity.P_user;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @Column(length = 100)
    private String createdBy;

    @Column(length = 100)
    private String updatedBy;

    @Column(length = 100)
    private String deletedBy;

    @Column(nullable = false)
    private Boolean deletedYn;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        deletedYn = false;
        setCreatedBy(getUserName());
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
        setUpdatedBy(getUserName());
    }

    private static String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl userDetailsImpl) {
            return userDetailsImpl.getUser().getNickname();
        }
        return null;
    }

    public void delete() {
        deletedAt = LocalDateTime.now();
        deletedYn = true;
        deletedBy = getUserName();
    }

}

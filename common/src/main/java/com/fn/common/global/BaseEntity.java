package com.fn.common.global;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

	@CreatedDate
	@Column(updatable = false, nullable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	private LocalDateTime updatedAt;

	@Column(nullable = false)
	private boolean isDeleted = false;

	private LocalDateTime deletedAt;

	@Column(updatable = false)
	private UUID createdBy;

	@Column(nullable = true)
	private UUID updatedBy;

	private UUID deletedBy;

	@PrePersist
	public void prePersist() {
		this.createdAt = LocalDateTime.now();
		this.createdBy = getAuthenticatedUserId(); // 직접 createdBy 설정
	}

	@PreUpdate
	public void preUpdate() {
		this.updatedAt = LocalDateTime.now();
		this.updatedBy = getAuthenticatedUserId(); // 직접 updatedBy 설정
		if (isDeleted && deletedAt == null) {
			deletedAt = LocalDateTime.now();
			deletedBy = getAuthenticatedUserId();
		}
	}

	public void markAsDeleted() {
		this.isDeleted = true;
	}

	private UUID getAuthenticatedUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			return null; // 인증되지 않은 경우 null 반환
		}

		Object principal = authentication.getPrincipal();
		if (principal instanceof UserDetails userDetails) {
			return UUID.fromString(userDetails.getUsername()); // userId가 username에 저장됨
		}
		return null;
	}
}


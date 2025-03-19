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

	private static final UUID SYSTEM_USER_UUID = UUID.fromString("00000000-0000-0000-0000-000000000001");
	private static final UUID ANONYMOUS_USER_UUID = UUID.fromString("00000000-0000-0000-0000-000000000002");

	private UUID getAuthenticatedUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// 1. 인증되지 않은 사용자일 경우 → SYSTEM UUID 반환
		if (authentication == null || !authentication.isAuthenticated()) {
			return SYSTEM_USER_UUID;
		}

		Object principal = authentication.getPrincipal();

		// 2. principal이 UserDetails가 아닐 경우 → ANONYMOUS UUID 반환
		if (!(principal instanceof UserDetails userDetails)) {
			return ANONYMOUS_USER_UUID;
		}

		String userIdStr = userDetails.getUsername();

		try {
			// 3. UUID 변환 성공 시 정상 반환
			return UUID.fromString(userIdStr);
		} catch (IllegalArgumentException e) {
			// 4. UUID 변환 실패 시 ANONYMOUS UUID 반환
			return ANONYMOUS_USER_UUID;
		}
	}

}


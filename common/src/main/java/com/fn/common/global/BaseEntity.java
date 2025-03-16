package com.fn.common.global;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

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

	@CreatedBy
	@Column(updatable = false)
	private String createdBy;

	@LastModifiedBy
	private String updatedBy;

	private String deletedBy;

	@PreUpdate
	public void setDeleted() {
		if (isDeleted) {
			deletedAt = LocalDateTime.now();
			// deletedBy = getAuthenticatedUsername();
		}
	}

	public void markAsDeleted() {
		this.isDeleted = true;
	}

	// private String getAuthenticatedUsername() {
	// 	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	// 	if (authentication == null || !authentication.isAuthenticated()) {
	// 		return "SYSTEM";
	// 	}
	// 	Object principal = authentication.getPrincipal();
	// 	return (principal instanceof UserDetails) ? ((UserDetails) principal).getUsername() : "UNKNOWN";
	// }
}

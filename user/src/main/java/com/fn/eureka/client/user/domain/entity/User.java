package com.fn.eureka.client.user.domain.entity;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_user")
public class User {
	@Id
	@UuidGenerator
	private UUID userId;

	@Column(nullable = false)
	private String userName;

	@Column(nullable = false)
	private String userPassword;

	@Column(nullable = false)
	private String userNickname;

	@Column(nullable = false, unique = true)
	private String userSlackId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserRole userRole;

	@Column(nullable = false, unique = true)
	private String userPhone;

	@Column(nullable = false)
	private String userEmail;

	// 정적 팩토리 메서드
	public static User of(String userName, String userPassword, String userNickname,
		String userSlackId, UserRole userRole, String userPhone, String userEmail) {

		return new User(userName, userPassword, userNickname, userSlackId, userRole, userPhone, userEmail);
	}

	// 프라이빗 생성자 (정적 팩토리 메서드만 사용하도록 제한)
	private User(String userName, String userPassword, String userNickname,
		String userSlackId, UserRole userRole, String userPhone, String userEmail) {
		this.userName = userName;
		this.userPassword = userPassword;
		this.userNickname = userNickname;
		this.userSlackId = userSlackId;
		this.userRole = userRole;
		this.userPhone = userPhone;
		this.userEmail = userEmail;
	}
}

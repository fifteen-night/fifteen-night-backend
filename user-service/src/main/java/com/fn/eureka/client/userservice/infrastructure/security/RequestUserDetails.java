package com.fn.eureka.client.userservice.infrastructure.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class RequestUserDetails implements UserDetails {

	private final String userId;
	private final String userName;
	private final Collection<? extends GrantedAuthority> authorities;

	public RequestUserDetails(String userId, String userName, Collection<? extends GrantedAuthority> authorities) {
		this.userId = userId;
		this.userName = userName;
		this.authorities = authorities;
	}

	public String getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return null; // Gateway에서 인증하므로 비밀번호 필요 없음
	}

	@Override
	public String getUsername() {
		return userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}

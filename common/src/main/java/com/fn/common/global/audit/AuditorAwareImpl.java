package com.fn.common.global.audit;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuditorAwareImpl implements AuditorAware<UUID> {

	@Override
	public Optional<UUID> getCurrentAuditor() {
		Authentication authentication = SecurityContextHelper.getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			return Optional.empty();
		}

		Object principal = authentication.getPrincipal();
		if (principal instanceof UserDetails userDetails) {
			try {
				return Optional.of(UUID.fromString(userDetails.getUsername()));
			} catch (IllegalArgumentException e) {
				return Optional.empty();
			}
		}
		return Optional.empty();
	}
}

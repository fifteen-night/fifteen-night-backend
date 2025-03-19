package com.fn.common.global.audit;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class SecurityContextHelper {

	private static final ThreadLocal<Authentication> authenticationThreadLocal = new ThreadLocal<>();

	public static void storeAuthentication() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		authenticationThreadLocal.set(auth);

		// 트랜잭션이 시작될 때 SecurityContext를 저장
		if (TransactionSynchronizationManager.isSynchronizationActive()) {
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
				@Override
				public void beforeCommit(boolean readOnly) {
					authenticationThreadLocal.set(SecurityContextHolder.getContext().getAuthentication());
				}

				@Override
				public void afterCompletion(int status) {
					authenticationThreadLocal.remove();
				}
			});
		}
	}

	public static Authentication getAuthentication() {
		return authenticationThreadLocal.get();
	}

	public static void clear() {
		authenticationThreadLocal.remove();
	}
}

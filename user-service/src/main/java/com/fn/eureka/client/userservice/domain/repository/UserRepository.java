package com.fn.eureka.client.userservice.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fn.eureka.client.userservice.domain.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {
	Optional<User> findByUserEmail(String userEmail);

	Optional<User> findByUserNickname(String userNickname);

	Optional<User> findByUserName(String userName);

	// 키워드가 없으면 전체 조회, 있으면 검색
	@Query("SELECT u FROM User u WHERE " +
		"(:keyword IS NULL OR " +
		"LOWER(u.userName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
		"LOWER(u.userNickname) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
		"LOWER(u.userEmail) LIKE LOWER(CONCAT('%', :keyword, '%')))")
	Page<User> findByKeyword(String keyword, Pageable pageable);
}

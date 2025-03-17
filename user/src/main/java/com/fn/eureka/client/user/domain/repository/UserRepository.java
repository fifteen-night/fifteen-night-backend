package com.fn.eureka.client.user.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fn.eureka.client.user.domain.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {
	Optional<User> findByUserEmail(String userEmail);

	Optional<User> findByUserNickname(String userNickname);

	Optional<User> findByUserName(String userName);
}

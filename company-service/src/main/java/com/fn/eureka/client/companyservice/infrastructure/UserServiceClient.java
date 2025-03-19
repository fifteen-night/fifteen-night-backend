package com.fn.eureka.client.companyservice.infrastructure;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fn.eureka.client.companyservice.application.dto.UserResponseDto;

import feign.FeignException;

@FeignClient(name = "user-service")
public interface UserServiceClient {

	// TODO user-service에 있는 메서드 이름이랑 일치시키기!!!

	// 유저 단건 조회
	@GetMapping("/users/{userId}")
	UserResponseDto getUserById(@PathVariable("userId") UUID userId);

	// 업체 생성 - companyManagerId로 유저 조회가 되면 true, 안되면 false
	default boolean checkExistUserIdInUserList(UUID companyManagerId) {
		try {
			getUserById(companyManagerId);
			return true;  // 정상적으로 응답을 받으면 true 반환
		} catch (FeignException.NotFound e) {
			return false; // 404 예외가 발생하면 false 반환
		} catch (Exception e) {
			throw new RuntimeException("해당 유저 존재 여부 확인 중 오류 발생", e);
		}
	}
}

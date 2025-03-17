package com.fn.eureka.client.company.infrastructure;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fn.eureka.client.company.application.dto.HubResponseDto;

import feign.FeignException;

@FeignClient(name = "hub-service")
public interface HubServiceClient {
	// TODO hub-service랑 메서드 이름 일치시키기!!!

	// 허브 조회
	@GetMapping("/hubs/{hubId}")
	HubResponseDto getHubById(@PathVariable("hubId") UUID hubId);

	// 업체 생성 - companyHubId로 허브 조회가 되면 true, 안되면 false
	default boolean checkExistHubIdInHubList(UUID companyHubId) {
		try {
			getHubById(companyHubId);
			return true;  // 정상적으로 응답을 받으면 true 반환
		} catch (FeignException.NotFound e) {
			return false; // 404 예외가 발생하면 false 반환
		} catch (Exception e) {
			throw new RuntimeException("해당 허브 존재 여부 확인 중 오류 발생", e);
		}
	}
}

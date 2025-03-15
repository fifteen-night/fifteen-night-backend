package com.fn.eureka.client.company.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fn.eureka.client.company.dto.HubResponseDto;

import feign.FeignException;

@FeignClient(name = "hub-service")
public interface HubServiceClient {

	// 해당 허브ID가 허브 리스트에 존재하면 true, 아니면 false
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

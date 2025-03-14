package com.fn.eureka.client.company.service;

import org.springframework.stereotype.Service;

import com.fn.common.global.exception.BadRequestException;

@Service
public class CompanyService {

	public void errorTest() {
		throw new BadRequestException();
	}
}

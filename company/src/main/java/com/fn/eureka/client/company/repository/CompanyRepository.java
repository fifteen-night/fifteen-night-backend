package com.fn.eureka.client.company.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fn.eureka.client.company.entity.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {

}

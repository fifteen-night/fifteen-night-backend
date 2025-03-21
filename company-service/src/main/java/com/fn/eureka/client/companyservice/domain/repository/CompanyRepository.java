package com.fn.eureka.client.companyservice.domain.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fn.eureka.client.companyservice.domain.entity.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {

}

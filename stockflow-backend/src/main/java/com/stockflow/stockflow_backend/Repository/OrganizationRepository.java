package com.stockflow.stockflow_backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stockflow.stockflow_backend.entity.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, Long>{

}

package com.stockflow.stockflow_backend.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stockflow.stockflow_backend.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
	 List<Product> findByOrganizationId(Long organizationId);

	    Optional<Product> findByIdAndOrganizationId(
	        Long id,
	        Long organizationId
	    );

	    boolean existsBySkuAndOrganizationId(
	        String sku,
	        Long organizationId
	    );

	    List<Product> findByOrganizationIdAndQuantityLessThanEqual(
	        Long organizationId,
	        Integer threshold
	    );

	    long countByOrganizationId(Long organizationId);

}

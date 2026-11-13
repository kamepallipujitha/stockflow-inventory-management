
package com.stockflow.stockflow_backend.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.stockflow.stockflow_backend.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByOrganizationId(Long organizationId);

    Page<Product> findByOrganizationId(Long organizationId, Pageable pageable);

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

    // Search products by name, SKU, or description within an organization
    @Query("""
        SELECT p FROM Product p
        WHERE p.organization.id = :organizationId
        AND (
            LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(p.sku) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(COALESCE(p.description, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
        """)
    Page<Product> searchProducts(
        @Param("organizationId") Long organizationId,
        @Param("keyword") String keyword,
        Pageable pageable
    );
}

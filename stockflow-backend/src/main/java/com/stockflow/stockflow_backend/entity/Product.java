package com.stockflow.stockflow_backend.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "products",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_product_sku_organization",
            columnNames = {"sku", "organization_id"}
        )
    }
)
public class Product {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(nullable = false)
	    private String name;

	    @Column(nullable = false)
	    private String sku;

	    @Column(length = 1000)
	    private String description;

	    @Column(nullable = false)
	    private Integer quantity;

	    @Column(name = "cost_price", nullable = false, precision = 12, scale = 2)
	    private BigDecimal costPrice;

	    @Column(name = "selling_price", nullable = false, precision = 12, scale = 2)
	    private BigDecimal sellingPrice;

	    @Column(name = "low_stock_threshold", nullable = false)
	    private Integer lowStockThreshold = 5;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "organization_id", nullable = false)
	    private Organization organization;

	    @Column(name = "created_at", nullable = false)
	    private LocalDateTime createdAt;

	    @Column(name = "updated_at")
	    private LocalDateTime updatedAt;

	    @PrePersist
	    public void onCreate() {
	        this.createdAt = LocalDateTime.now();
	        this.updatedAt = LocalDateTime.now();
	    }

	    @PreUpdate
	    public void onUpdate() {
	        this.updatedAt = LocalDateTime.now();
	    }

	    public Product() {
	    }

	    public Long getId() {
	        return id;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public String getSku() {
	        return sku;
	    }

	    public void setSku(String sku) {
	        this.sku = sku;
	    }

	    public String getDescription() {
	        return description;
	    }

	    public void setDescription(String description) {
	        this.description = description;
	    }

	    public Integer getQuantity() {
	        return quantity;
	    }

	    public void setQuantity(Integer quantity) {
	        this.quantity = quantity;
	    }

	    public BigDecimal getCostPrice() {
	        return costPrice;
	    }

	    public void setCostPrice(BigDecimal costPrice) {
	        this.costPrice = costPrice;
	    }

	    public BigDecimal getSellingPrice() {
	        return sellingPrice;
	    }

	    public void setSellingPrice(BigDecimal sellingPrice) {
	        this.sellingPrice = sellingPrice;
	    }

	    public Integer getLowStockThreshold() {
	        return lowStockThreshold;
	    }

	    public void setLowStockThreshold(Integer lowStockThreshold) {
	        this.lowStockThreshold = lowStockThreshold;
	    }

	    public Organization getOrganization() {
	        return organization;
	    }

	    public void setOrganization(Organization organization) {
	        this.organization = organization;
	    }

	    public LocalDateTime getCreatedAt() {
	        return createdAt;
	    }

	    public LocalDateTime getUpdatedAt() {
	        return updatedAt;
	    }
}

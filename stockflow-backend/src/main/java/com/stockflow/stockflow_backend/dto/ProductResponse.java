package com.stockflow.stockflow_backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductResponse {
	 private Long id;
	    private String name;
	    private String sku;
	    private String description;
	    private Integer quantity;
	    private BigDecimal costPrice;
	    private BigDecimal sellingPrice;
	    private Integer lowStockThreshold;
	    private LocalDateTime createdAt;
	    private LocalDateTime updatedAt;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
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
		public LocalDateTime getCreatedAt() {
			return createdAt;
		}
		public void setCreatedAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
		}
		public LocalDateTime getUpdatedAt() {
			return updatedAt;
		}
		public void setUpdatedAt(LocalDateTime updatedAt) {
			this.updatedAt = updatedAt;
		}
	    
	    
}

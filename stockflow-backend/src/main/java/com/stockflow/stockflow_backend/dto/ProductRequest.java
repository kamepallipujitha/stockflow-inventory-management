package com.stockflow.stockflow_backend.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class ProductRequest {
	 @NotBlank
	    private String name;

	    @NotBlank
	    private String sku;

	    private String description;

	    @NotNull
	    @PositiveOrZero
	    private Integer quantity;

	    @NotNull
	    @PositiveOrZero
	    private BigDecimal costPrice;

	    @NotNull
	    @PositiveOrZero
	    private BigDecimal sellingPrice;

	    @PositiveOrZero
	    private Integer lowStockThreshold;

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
	    

}

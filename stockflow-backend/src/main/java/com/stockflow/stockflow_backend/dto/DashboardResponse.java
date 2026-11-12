package com.stockflow.stockflow_backend.dto;

import java.util.List;

public class DashboardResponse {
	  private long totalProducts;
	    private long totalQuantity;
	    private List<ProductResponse> lowStockProducts;

	    public DashboardResponse(long totalProducts,
	            long totalQuantity,
	            List<ProductResponse> lowStockProducts) {

	        this.totalProducts = totalProducts;
	        this.totalQuantity = totalQuantity;
	        this.lowStockProducts = lowStockProducts;
	    }

	    public long getTotalProducts() {
	        return totalProducts;
	    }

	    public long getTotalQuantity() {
	        return totalQuantity;
	    }

	    public List<ProductResponse> getLowStockProducts() {
	        return lowStockProducts;
	    }
}

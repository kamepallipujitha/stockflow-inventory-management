package com.stockflow.stockflow_backend.Service;

import java.util.List;

import com.stockflow.stockflow_backend.dto.DashboardResponse;
import com.stockflow.stockflow_backend.dto.ProductRequest;
import com.stockflow.stockflow_backend.dto.ProductResponse;

public interface ProductService {
	ProductResponse createProduct(ProductRequest request);
	List<ProductResponse> getAllProducts();
	ProductResponse getProductById(Long id);
	
	ProductResponse updateProduct(Long id, ProductRequest request);
	void deleteProduct(Long id);
	DashboardResponse getDashboard();
	
}

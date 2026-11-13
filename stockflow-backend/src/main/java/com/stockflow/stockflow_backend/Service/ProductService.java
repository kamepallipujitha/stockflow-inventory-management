
package com.stockflow.stockflow_backend.Service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.stockflow.stockflow_backend.dto.DashboardResponse;
import com.stockflow.stockflow_backend.dto.ProductRequest;
import com.stockflow.stockflow_backend.dto.ProductResponse;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);

    List<ProductResponse> getAllProducts();

    // Pagination and sorting
    Page<ProductResponse> getAllProducts(
        int page,
        int size,
        String sortBy,
        String direction
    );

    // Search with pagination and sorting
    Page<ProductResponse> searchProducts(
        String keyword,
        int page,
        int size,
        String sortBy,
        String direction
    );

    ProductResponse getProductById(Long id);

    ProductResponse updateProduct(Long id, ProductRequest request);

    void deleteProduct(Long id);

    DashboardResponse getDashboard();
}

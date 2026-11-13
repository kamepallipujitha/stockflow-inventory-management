
package com.stockflow.stockflow_backend.Controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stockflow.stockflow_backend.Service.ProductService;
import com.stockflow.stockflow_backend.dto.DashboardResponse;
import com.stockflow.stockflow_backend.dto.ProductRequest;
import com.stockflow.stockflow_backend.dto.ProductResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Create Product
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest request) {

        ProductResponse response =
                productService.createProduct(request);

        return new ResponseEntity<>(
                response, HttpStatus.CREATED);
    }

    // Get All Products (existing endpoint)
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {

        List<ProductResponse> products =
                productService.getAllProducts();

        return ResponseEntity.ok(products);
    }

    // Get Products with Pagination and Sorting
    @GetMapping("/paged")
    public ResponseEntity<Page<ProductResponse>> getAllProductsPaged(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "5") int size,

            @RequestParam(defaultValue = "name") String sortBy,

            @RequestParam(defaultValue = "asc") String direction) {

        Page<ProductResponse> products =
                productService.getAllProducts(
                        page,
                        size,
                        sortBy,
                        direction
                );

        return ResponseEntity.ok(products);
    }

    // Get Product By ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(
            @PathVariable Long id) {

        ProductResponse product =
                productService.getProductById(id);

        return ResponseEntity.ok(product);
    }

    // Update Product
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {

        ProductResponse updatedProduct =
                productService.updateProduct(id, request);

        return ResponseEntity.ok(updatedProduct);
    }

    // Delete Product
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(
            @PathVariable Long id) {

        productService.deleteProduct(id);

        return ResponseEntity.ok(
                "Product deleted successfully");
    }

    // Dashboard
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponse> getDashboard() {

        DashboardResponse dashboard =
                productService.getDashboard();

        return ResponseEntity.ok(dashboard);
    }

    // Search Products with Pagination and Sorting
    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponse>> searchProducts(

            @RequestParam(defaultValue = "") String keyword,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "5") int size,

            @RequestParam(defaultValue = "name") String sortBy,

            @RequestParam(defaultValue = "asc") String direction) {

        Page<ProductResponse> products =
                productService.searchProducts(
                        keyword,
                        page,
                        size,
                        sortBy,
                        direction
                );

        return ResponseEntity.ok(products);
    }

}

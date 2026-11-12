package com.stockflow.stockflow_backend.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
	 // Get All Products
	    @GetMapping
	    public ResponseEntity<List<ProductResponse>> getAllProducts() {

	        List<ProductResponse> products =
	                productService.getAllProducts();

	        return ResponseEntity.ok(products);
	    }
	    @GetMapping("/{id}")
	    public ResponseEntity<ProductResponse> getProductById(
	            @PathVariable Long id) {

	        ProductResponse product = productService.getProductById(id);

	        return ResponseEntity.ok(product);
	    }
	    @PutMapping("/{id}")
	    public ResponseEntity<ProductResponse> updateProduct(
	            @PathVariable Long id,
	            @Valid @RequestBody ProductRequest request) {

	        ProductResponse updatedProduct =
	                productService.updateProduct(id, request);

	        return ResponseEntity.ok(updatedProduct);
	    }
	    @DeleteMapping("/{id}")
	    public ResponseEntity<String> deleteProduct(
	            @PathVariable Long id) {

	        productService.deleteProduct(id);

	        return ResponseEntity.ok("Product deleted successfully");
	    }
	    @GetMapping("/dashboard")
	    public ResponseEntity<DashboardResponse> getDashboard() {

	        DashboardResponse dashboard =
	                productService.getDashboard();

	        return ResponseEntity.ok(dashboard);
	    }
}

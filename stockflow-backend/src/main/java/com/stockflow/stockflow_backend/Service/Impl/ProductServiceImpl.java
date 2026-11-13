package com.stockflow.stockflow_backend.Service.Impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.stockflow.stockflow_backend.Repository.ProductRepository;
import com.stockflow.stockflow_backend.Repository.UserRepository;
import com.stockflow.stockflow_backend.Service.ProductService;
import com.stockflow.stockflow_backend.dto.DashboardResponse;
import com.stockflow.stockflow_backend.dto.ProductRequest;
import com.stockflow.stockflow_backend.dto.ProductResponse;
import com.stockflow.stockflow_backend.entity.AppUser;
import com.stockflow.stockflow_backend.entity.Product;

import jakarta.transaction.Transactional;

@Service
public class ProductServiceImpl implements ProductService {
	 private final ProductRepository productRepository;
	    private final UserRepository userRepository;

	    public ProductServiceImpl(
	            ProductRepository productRepository,
	            UserRepository userRepository) {

	        this.productRepository = productRepository;
	        this.userRepository = userRepository;
	    }

	    @Transactional
	    public ProductResponse createProduct(ProductRequest request) {

	        // Get logged-in user's email
	        String email = SecurityContextHolder.getContext()
	                .getAuthentication().getName();

	        // Find logged-in user
	        AppUser user = userRepository.findByEmail(email)
	                .orElseThrow(() ->
	                        new RuntimeException("User not found"));

	        // Check duplicate SKU within the user's organization
	        Long organizationId = user.getOrganization().getId();

	        if (productRepository.existsBySkuAndOrganizationId(
	                request.getSku(), organizationId)) {

	            throw new RuntimeException(
	                    "Product SKU already exists in your organization");
	        }

	        // Create product
	        Product product = new Product();

	        product.setName(request.getName());
	        product.setSku(request.getSku());
	        product.setDescription(request.getDescription());
	        product.setQuantity(request.getQuantity());
	        product.setCostPrice(request.getCostPrice());
	        product.setSellingPrice(request.getSellingPrice());
	        product.setLowStockThreshold(request.getLowStockThreshold());
	        product.setOrganization(user.getOrganization());

	        // Save product
	        Product savedProduct = productRepository.save(product);

	        // Convert entity to response DTO
	        ProductResponse response = new ProductResponse();

	        response.setId(savedProduct.getId());
	        response.setName(savedProduct.getName());
	        response.setSku(savedProduct.getSku());
	        response.setDescription(savedProduct.getDescription());
	        response.setQuantity(savedProduct.getQuantity());
	        response.setCostPrice(savedProduct.getCostPrice());
	        response.setSellingPrice(savedProduct.getSellingPrice());
	        response.setLowStockThreshold(
	                savedProduct.getLowStockThreshold());

	        return response;
	    }

		@Override
		public List<ProductResponse> getAllProducts() {
			  // Get logged-in user's email
		    String email = SecurityContextHolder.getContext()
		            .getAuthentication().getName();

		    // Find logged-in user
		    AppUser user = userRepository.findByEmail(email)
		            .orElseThrow(() ->
		                    new RuntimeException("User not found"));

		    // Get products belonging to this organization
		    Long organizationId = user.getOrganization().getId();

		    List<Product> products =
		            productRepository.findByOrganizationId(organizationId);

		    // Convert Product entities into ProductResponse DTOs
		    return products.stream().map(product -> {

		        ProductResponse response = new ProductResponse();

		        response.setId(product.getId());
		        response.setName(product.getName());
		        response.setSku(product.getSku());
		        response.setDescription(product.getDescription());
		        response.setQuantity(product.getQuantity());
		        response.setCostPrice(product.getCostPrice());
		        response.setSellingPrice(product.getSellingPrice());
		        response.setLowStockThreshold(
		                product.getLowStockThreshold());

		        return response;

		    }).collect(Collectors.toList());
		
		}
		
		@Override
		@Transactional
		public ProductResponse getProductById(Long id) {

		    String email = SecurityContextHolder.getContext()
		            .getAuthentication().getName();

		    AppUser user = userRepository.findByEmail(email)
		            .orElseThrow(() ->
		                    new RuntimeException("User not found"));

		    Long organizationId = user.getOrganization().getId();

		    Product product = productRepository
		            .findByIdAndOrganizationId(id, organizationId)
		            .orElseThrow(() ->
		                    new RuntimeException("Product not found"));

		    ProductResponse response = new ProductResponse();

		    response.setId(product.getId());
		    response.setName(product.getName());
		    response.setSku(product.getSku());
		    response.setDescription(product.getDescription());
		    response.setQuantity(product.getQuantity());
		    response.setCostPrice(product.getCostPrice());
		    response.setSellingPrice(product.getSellingPrice());
		    response.setLowStockThreshold(product.getLowStockThreshold());

		    return response;
		}
		@Override
		@Transactional
		public ProductResponse updateProduct(
		        Long id, ProductRequest request) {

		    String email = SecurityContextHolder.getContext()
		            .getAuthentication().getName();

		    AppUser user = userRepository.findByEmail(email)
		            .orElseThrow(() ->
		                    new RuntimeException("User not found"));

		    Long organizationId = user.getOrganization().getId();

		    Product product = productRepository
		            .findByIdAndOrganizationId(id, organizationId)
		            .orElseThrow(() ->
		                    new RuntimeException("Product not found"));

		    // Check duplicate SKU
		    if (!product.getSku().equals(request.getSku()) &&
		            productRepository.existsBySkuAndOrganizationId(
		                    request.getSku(), organizationId)) {

		        throw new RuntimeException("SKU already exists");
		    }

		    product.setName(request.getName());
		    product.setSku(request.getSku());
		    product.setDescription(request.getDescription());
		    product.setQuantity(request.getQuantity());
		    product.setCostPrice(request.getCostPrice());
		    product.setSellingPrice(request.getSellingPrice());
		    product.setLowStockThreshold(request.getLowStockThreshold());

		    Product updatedProduct = productRepository.save(product);

		    ProductResponse response = new ProductResponse();

		    response.setId(updatedProduct.getId());
		    response.setName(updatedProduct.getName());
		    response.setSku(updatedProduct.getSku());
		    response.setDescription(updatedProduct.getDescription());
		    response.setQuantity(updatedProduct.getQuantity());
		    response.setCostPrice(updatedProduct.getCostPrice());
		    response.setSellingPrice(updatedProduct.getSellingPrice());
		    response.setLowStockThreshold(
		            updatedProduct.getLowStockThreshold());

		    return response;
		}
		@Override
		@Transactional
		public void deleteProduct(Long id) {

		    String email = SecurityContextHolder.getContext()
		            .getAuthentication().getName();

		    AppUser user = userRepository.findByEmail(email)
		            .orElseThrow(() ->
		                    new RuntimeException("User not found"));

		    Long organizationId = user.getOrganization().getId();

		    Product product = productRepository
		            .findByIdAndOrganizationId(id, organizationId)
		            .orElseThrow(() ->
		                    new RuntimeException("Product not found"));

		    productRepository.delete(product);
		}
		@Override
		@Transactional
		public DashboardResponse getDashboard() {

		    String email = SecurityContextHolder.getContext()
		            .getAuthentication().getName();

		    AppUser user = userRepository.findByEmail(email)
		            .orElseThrow(() ->
		                    new RuntimeException("User not found"));

		    Long organizationId = user.getOrganization().getId();

		    List<Product> products =
		            productRepository.findByOrganizationId(organizationId);

		    // Total number of products
		    long totalProducts = products.size();

		    // Total quantity of all products
		    long totalQuantity = products.stream()
		            .mapToLong(Product::getQuantity)
		            .sum();

		    // Find low-stock products using each product's threshold
		    List<ProductResponse> lowStockProducts = products.stream()
		            .filter(product ->
		                    product.getQuantity() <=
		                    product.getLowStockThreshold())
		            .map(product -> {

		                ProductResponse response = new ProductResponse();

		                response.setId(product.getId());
		                response.setName(product.getName());
		                response.setSku(product.getSku());
		                response.setDescription(product.getDescription());
		                response.setQuantity(product.getQuantity());
		                response.setCostPrice(product.getCostPrice());
		                response.setSellingPrice(product.getSellingPrice());
		                response.setLowStockThreshold(
		                        product.getLowStockThreshold());

		                return response;
		            })
		            .collect(Collectors.toList());

		    return new DashboardResponse(
		            totalProducts,
		            totalQuantity,
		            lowStockProducts
		    );
		}

@Override

@Transactional

public Page<ProductResponse> getAllProducts(
        int page,
        int size,
        String sortBy,
        String direction) {

    // Validate page number and page size
    if (page < 0) {
        throw new IllegalArgumentException(
                "Page number cannot be negative");
    }

    if (size < 1 || size > 100) {
        throw new IllegalArgumentException(
                "Page size must be between 1 and 100");
    }

    // Allow only valid Product entity fields for sorting
    Set<String> allowedFields = Set.of(
            "name",
            "sku",
            "quantity",
            "costPrice",
            "sellingPrice",
            "createdAt",
            "updatedAt"
    );

    if (sortBy == null || !allowedFields.contains(sortBy)) {
        throw new IllegalArgumentException(
                "Invalid sorting field");
    }

    // Validate sorting direction
    Sort.Direction sortDirection;

    try {
        sortDirection = Sort.Direction.fromString(direction);
    } catch (IllegalArgumentException | NullPointerException e) {
        throw new IllegalArgumentException(
                "Direction must be asc or desc");
    }

    // Get logged-in user's email
    String email = SecurityContextHolder.getContext()
            .getAuthentication().getName();

    // Find logged-in user
    AppUser user = userRepository.findByEmail(email)
            .orElseThrow(() ->
                    new RuntimeException("User not found"));

    // Get organization ID
    Long organizationId = user.getOrganization().getId();

    // Create pagination and sorting request
    Pageable pageable = PageRequest.of(
            page,
            size,
            Sort.by(sortDirection, sortBy)
    );

    // Fetch only this organization's products
    Page<Product> products =
            productRepository.findByOrganizationId(
                    organizationId,
                    pageable
            );

    // Convert entities into response DTOs
    return products.map(product -> {
        ProductResponse response = new ProductResponse();

        response.setId(product.getId());
        response.setName(product.getName());
        response.setSku(product.getSku());
        response.setDescription(product.getDescription());
        response.setQuantity(product.getQuantity());
        response.setCostPrice(product.getCostPrice());
        response.setSellingPrice(product.getSellingPrice());
        response.setLowStockThreshold(
                product.getLowStockThreshold());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());

        return response;
    });
}

@Override
@Transactional
public Page<ProductResponse> searchProducts(
        String keyword,
        int page,
        int size,
        String sortBy,
        String direction) {

    // Validate pagination
    if (page < 0) {
        throw new IllegalArgumentException(
                "Page number cannot be negative");
    }

    if (size < 1 || size > 100) {
        throw new IllegalArgumentException(
                "Page size must be between 1 and 100");
    }

    // Validate sorting fields
    Set<String> allowedFields = Set.of(
            "name",
            "sku",
            "quantity",
            "costPrice",
            "sellingPrice",
            "createdAt",
            "updatedAt"
    );

    if (sortBy == null || !allowedFields.contains(sortBy)) {
        throw new IllegalArgumentException(
                "Invalid sorting field");
    }

    // Validate sorting direction
    Sort.Direction sortDirection;

    try {
        sortDirection = Sort.Direction.fromString(direction);
    } catch (IllegalArgumentException | NullPointerException e) {
        throw new IllegalArgumentException(
                "Direction must be asc or desc");
    }

    // Get logged-in user
    String email = SecurityContextHolder.getContext()
            .getAuthentication().getName();

    AppUser user = userRepository.findByEmail(email)
            .orElseThrow(() ->
                    new RuntimeException("User not found"));

    Long organizationId = user.getOrganization().getId();

    // Prepare search keyword
    String searchKeyword = keyword == null ? "" : keyword.trim();

    // Create pagination and sorting
    Pageable pageable = PageRequest.of(
            page,
            size,
            Sort.by(sortDirection, sortBy)
    );

    // Search only within the logged-in user's organization
    Page<Product> products = productRepository.searchProducts(
            organizationId,
            searchKeyword,
            pageable
    );

    // Convert Product entities into ProductResponse DTOs
    return products.map(product -> {
        ProductResponse response = new ProductResponse();

        response.setId(product.getId());
        response.setName(product.getName());
        response.setSku(product.getSku());
        response.setDescription(product.getDescription());
        response.setQuantity(product.getQuantity());
        response.setCostPrice(product.getCostPrice());
        response.setSellingPrice(product.getSellingPrice());
        response.setLowStockThreshold(
                product.getLowStockThreshold());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());

        return response;
    });
}


}

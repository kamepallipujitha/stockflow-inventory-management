package com.stockflow.stockflow_backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;


@Entity
@Table(name = "organizations")

public class Organization {
	   @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(nullable = false)
	    private String name;

	    @Column(name = "low_stock_threshold", nullable = false)
	    private Integer lowStockThreshold = 5;

	    @Column(name = "created_at", nullable = false)
	    private LocalDateTime createdAt;
	    
	    @PrePersist
	    public void onCreate() {
	        this.createdAt = LocalDateTime.now();
	    }

	    public Organization() {
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

	    public Integer getLowStockThreshold() {
	        return lowStockThreshold;
	    }

	    public void setLowStockThreshold(Integer lowStockThreshold) {
	        this.lowStockThreshold = lowStockThreshold;
	    }

	    public LocalDateTime getCreatedAt() {
	        return createdAt;
	    }

}

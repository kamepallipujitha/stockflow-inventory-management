package com.stockflow.stockflow_backend.entity;

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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
	@Table(
	    name = "users",
	    uniqueConstraints = {
	        @UniqueConstraint(columnNames = "email")
	    }
	)
	public class AppUser {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(nullable = false)
	    private String name;

	    @Column(nullable = false, unique = true)
	    private String email;

	    @Column(nullable = false)
	    private String password;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "organization_id", nullable = false)
	    private Organization organization;

	    @Column(name = "created_at", nullable = false)
	    private LocalDateTime createdAt;

	    @PrePersist
	    public void onCreate() {
	        this.createdAt = LocalDateTime.now();
	    }

	    public AppUser() {
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

	    public String getEmail() {
	        return email;
	    }

	    public void setEmail(String email) {
	        this.email = email;
	    }

	    public String getPassword() {
	        return password;
	    }

	    public void setPassword(String password) {
	        this.password = password;
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

}

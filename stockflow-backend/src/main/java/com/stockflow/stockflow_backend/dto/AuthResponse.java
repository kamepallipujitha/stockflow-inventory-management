package com.stockflow.stockflow_backend.dto;

public class AuthResponse {
	 private String token;
	    private String name;
	    private String email;
	    private Long organizationId;

	    public AuthResponse(String token, String name, String email, Long organizationId) {
	        this.token = token;
	        this.name = name;
	        this.email = email;
	        this.organizationId = organizationId;
	    }

	    public String getToken() {
	        return token;
	    }

	    public String getName() {
	        return name;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public Long getOrganizationId() {
	        return organizationId;
	    }
}

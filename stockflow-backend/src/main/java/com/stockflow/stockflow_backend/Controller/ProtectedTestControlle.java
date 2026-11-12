package com.stockflow.stockflow_backend.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProtectedTestControlle {
	 @GetMapping("/api/test/protected")
	    public String protectedTest() {
	        return "JWT Authentication Successful!";
	    }
}

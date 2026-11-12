package com.stockflow.stockflow_backend.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stockflow.stockflow_backend.entity.AppUser;

public interface UserRepository extends JpaRepository<AppUser, Long> {
	 Optional<AppUser> findByEmail(String email);

	    boolean existsByEmail(String email);
}

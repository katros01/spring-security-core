package com.gtp2.spring.security.core.repositories;

import com.gtp2.spring.security.core.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    boolean existsByName(String name);

    List<Product> findByUserId(String userId);
}

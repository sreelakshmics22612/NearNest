package com.nearnest.repository;

import com.nearnest.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByBusinessId(Long businessId);
}
package com.nearnest.service;

import com.nearnest.model.Product;

import java.util.List;

public interface ProductService {

    Product addProduct(Product product);

    List<Product> getProductsByBusinessId(Long businessId);

    Product getProductById(Long id);

    Product updateProduct(Long id, Product product);

    void deleteProduct(Long id);

    Product updateAvailability(Long id, boolean available);
}
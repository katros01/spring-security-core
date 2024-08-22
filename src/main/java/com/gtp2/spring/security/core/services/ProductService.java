package com.gtp2.spring.security.core.services;

import com.gtp2.spring.security.core.dto.ProductDto;
import com.gtp2.spring.security.core.models.Product;
import com.gtp2.spring.security.core.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public boolean nameExists(String name) {
        return productRepository.existsByName(name);
    }

    public Product createProduct(ProductDto productDto, String userId) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        product.setUserId(userId);

        return productRepository.save(product);
    }

    public Product updateProduct(String productId, ProductDto productDto, String userId) {
        Optional<Product> existingProduct = productRepository.findById(productId);
        if (existingProduct.isEmpty()) {
            throw new RuntimeException("Product not found");
        }

        Product product = existingProduct.get();
        if (!product.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }

        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());

        return productRepository.save(product);
    }

    public Product getProductById(String productId, String userId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!product.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }

        return product;
    }

    public List<Product> getAllSellerProducts(String userId) {
        // If you want to get all products for a specific user
        return productRepository.findByUserId(userId);
    }

    public List<Product> getAllProducts() {
        // If you want to get all products for a specific user
        return productRepository.findAll();
    }

    public void deleteProduct(String productId, String userId) {
        Product product = getProductById(productId, userId);
        productRepository.delete(product);
    }
}

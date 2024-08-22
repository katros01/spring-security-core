package com.gtp2.spring.security.core.controllers;

import com.gtp2.spring.security.core.dto.ProductDto;
import com.gtp2.spring.security.core.models.Product;
import com.gtp2.spring.security.core.services.ProductService;
import com.gtp2.spring.security.core.services.UserService;
import com.gtp2.spring.security.core.utils.CustomResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    private String getCurrentUserId() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();
            return userService.findByEmail(email).getId();      // Assuming email is used as the user ID
        }
        throw new RuntimeException("User not authenticated");
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SELLER')")
    public ResponseEntity<CustomResponse<?>> createProduct(@Valid @RequestBody ProductDto productBody, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }

            CustomResponse<Map<String, String>> response = new CustomResponse<>(
                    "Validation failed",
                    HttpStatus.BAD_REQUEST.value(),
                    errors
            );

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (productService.nameExists(productBody.getName())) {
            Map<String, String> errors = new HashMap<>();
            errors.put("name", "Name already exists");

            CustomResponse<Map<String, String>> response = new CustomResponse<>(
                    "Validation failed",
                    HttpStatus.BAD_REQUEST.value(),
                    errors
            );

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        String userId = getCurrentUserId();
        Product product = productService.createProduct(productBody, userId);
        return new ResponseEntity<>(new CustomResponse<>("Product created successfully", HttpStatus.CREATED.value(), product), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomResponse<Product>> updateProduct(@PathVariable String id, @Valid @RequestBody ProductDto productDto) throws Exception {
        String userId = getCurrentUserId();
        Product product = productService.updateProduct(id, productDto, userId);
        return new ResponseEntity<>(new CustomResponse<>("Product updated successfully", HttpStatus.OK.value(), product), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse<Product>> getProductById(@PathVariable String id) throws Exception {
        String userId = getCurrentUserId();
        Product product = productService.getProductById(id, userId);
        return new ResponseEntity<>(new CustomResponse<>("Product retrieved successfully", HttpStatus.OK.value(), product), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<CustomResponse<List<Product>>> getAllProducts() throws Exception {
        List<Product> products;
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        System.out.println(role);

        if (role.equals("ROLE_SELLER")) {
            String userId = getCurrentUserId();
            products = productService.getAllSellerProducts(userId);
        } else {
            products = productService.getAllProducts();
        }
        return new ResponseEntity<>(new CustomResponse<>("Products retrieved successfully", HttpStatus.OK.value(), products), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse<Void>> deleteProduct(@PathVariable String id) throws Exception {
        String userId = getCurrentUserId();
        productService.deleteProduct(id, userId);
        return new ResponseEntity<>(new CustomResponse<>("Product deleted successfully", HttpStatus.OK.value(), null), HttpStatus.OK);
    }
}

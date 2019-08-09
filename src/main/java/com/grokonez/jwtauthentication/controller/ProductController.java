package com.grokonez.jwtauthentication.controller;


import com.grokonez.jwtauthentication.model.Product;
import com.grokonez.jwtauthentication.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {
//@TODO implement sorting, search by name, type
    @Autowired
    ProductRepository productRepository;

    @GetMapping("/product")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
//    public List <Product> getAllProducts(@PathVariable("order_by") String field) {
    public List <Product> getAllProducts() {
        return productRepository.findAll();
    }

    @PostMapping("/product")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity <Product> create(@RequestBody Product product){
        productRepository.save(product);
        return ResponseEntity.ok().body(product);
    }

    @GetMapping("/product/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Product getProductById(@PathVariable(value="id") Long productId){
        return productRepository.findById(productId)
                .orElseThrow(() ->
                new EntityNotFoundException("Product Not Found with -> productId : " + productId)
        );
    }

    @DeleteMapping("/product/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteProduct(Product product) {
        productRepository.delete(product);
    }

}
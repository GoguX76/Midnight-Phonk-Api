package com.example.midnight_phonk_api.Midnight_Phonk_Api.controller;

import com.example.midnight_phonk_api.Midnight_Phonk_Api.model.Products;
import com.example.midnight_phonk_api.Midnight_Phonk_Api.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<List<Products>> getAllProducts() {
        List<Products> products = productRepository.findAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Products> getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Products> createProduct(@Valid @RequestBody Products product) {
        Products savedProduct = productRepository.save(product);
        return ResponseEntity.ok(savedProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Products> updateProduct(@PathVariable Long id, @Valid @RequestBody Products productDetails) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setTitle(productDetails.getTitle());
                    product.setImage(productDetails.getImage());
                    product.setShortDesc(productDetails.getShortDesc());
                    product.setFullDesc(productDetails.getFullDesc());
                    product.setPrice(productDetails.getPrice());
                    product.setStock(productDetails.getStock());
                    Products updatedProduct = productRepository.save(product);
                    return ResponseEntity.ok(updatedProduct);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Products> patchProduct(@PathVariable Long id, @RequestBody Products productDetails) {
        return productRepository.findById(id)
                .map(product -> {
                    if (productDetails.getTitle() != null) product.setTitle(productDetails.getTitle());
                    if (productDetails.getImage() != null) product.setImage(productDetails.getImage());
                    if (productDetails.getDesc() != null) product.setDesc(productDetails.getDesc());
                    if (productDetails.getFullDesc() != null) product.setFullDesc(productDetails.getFullDesc());
                    if (productDetails.getPrice() != 0) product.setPrice(productDetails.getPrice());
                    if (productDetails.getStock() != null) product.setStock(productDetails.getStock());
                    Products updatedProduct = productRepository.save(product);
                    return ResponseEntity.ok(updatedProduct);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    productRepository.delete(product);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}

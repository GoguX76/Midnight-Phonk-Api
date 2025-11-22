package com.example.midnight_phonk_api.Midnight_Phonk_Api.service;

import com.example.midnight_phonk_api.Midnight_Phonk_Api.model.Products;
import com.example.midnight_phonk_api.Midnight_Phonk_Api.model.Purchases;
import com.example.midnight_phonk_api.Midnight_Phonk_Api.model.Users;
import com.example.midnight_phonk_api.Midnight_Phonk_Api.repository.ProductRepository;
import com.example.midnight_phonk_api.Midnight_Phonk_Api.repository.PurchasesRepository;
import com.example.midnight_phonk_api.Midnight_Phonk_Api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PurchasesService {

    @Autowired
    private PurchasesRepository purchasesRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<Purchases> getAllPurchases() {
        return purchasesRepository.findAll();
    }

    public Optional<Purchases> getPurchaseById(Long id) {
        return purchasesRepository.findById(id);
    }

    public List<Purchases> getPurchasesByUserId(Long userId) {
        return purchasesRepository.findByUserId(userId);
    }

    public List<Purchases> getPurchasesByProductId(Long productId) {
        return purchasesRepository.findByProductId(productId);
    }

    public Purchases createPurchase(Long userId, Long productId, Integer quantity) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Double totalPrice = product.getPrice() * quantity;

        Purchases purchase = Purchases.builder()
                .user(user)
                .product(product)
                .quantity(quantity)
                .totalPrice(totalPrice)
                .build();

        return purchasesRepository.save(purchase);
    }

    public void deletePurchase(Long id) {
        purchasesRepository.deleteById(id);
    }
}

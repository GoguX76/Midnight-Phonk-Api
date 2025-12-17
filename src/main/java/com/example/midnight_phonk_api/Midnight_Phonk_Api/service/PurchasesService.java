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

    @Autowired
    private com.example.midnight_phonk_api.Midnight_Phonk_Api.repository.ReceiptRepository receiptRepository;

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

    @org.springframework.transaction.annotation.Transactional
    public List<Purchases> createPurchase(
            com.example.midnight_phonk_api.Midnight_Phonk_Api.dto.PurchaseRequest request) {
        Users user = userRepository.findByEmail(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + request.getUserId()));

        String shippingDetailsJson = "";
        try {
            shippingDetailsJson = new com.fasterxml.jackson.databind.ObjectMapper()
                    .writeValueAsString(request.getShippingDetails());
        } catch (Exception e) {
            shippingDetailsJson = String.valueOf(request.getShippingDetails());
        }

        String finalShippingDetails = shippingDetailsJson;

        String orderCode = (request.getOrderCode() != null && !request.getOrderCode().isEmpty())
                ? request.getOrderCode()
                : java.util.UUID.randomUUID().toString();

        List<Purchases> purchasesList = request.getItems().stream().map(item -> {
            Products product = productRepository.findById(item.getId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + item.getId()));

            Double totalPrice = product.getPrice() * item.getQuantity();

            Purchases purchase = Purchases.builder()
                    .user(user)
                    .product(product)
                    .quantity(item.getQuantity())
                    .totalPrice(totalPrice)
                    .shippingDetails(finalShippingDetails)
                    .orderCode(orderCode)
                    .build();

            return purchasesRepository.save(purchase);
        }).collect(java.util.stream.Collectors.toList());

        if (request.getReceiptPdfBase64() != null && !request.getReceiptPdfBase64().isEmpty()) {
            try {
                byte[] pdfContent = java.util.Base64.getDecoder().decode(request.getReceiptPdfBase64());
                com.example.midnight_phonk_api.Midnight_Phonk_Api.model.Receipt receipt = com.example.midnight_phonk_api.Midnight_Phonk_Api.model.Receipt
                        .builder()
                        .orderCode(orderCode)
                        .content(pdfContent)
                        .build();

                receiptRepository.save(receipt);
            } catch (Exception e) {
                System.err.println("Error saving receipt PDF: " + e.getMessage());
            }
        }

        return purchasesList;
    }

    public void deletePurchase(Long id) {
        purchasesRepository.deleteById(id);
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Optional<com.example.midnight_phonk_api.Midnight_Phonk_Api.model.Receipt> getReceiptByOrderCode(
            String orderCode) {
        return receiptRepository.findByOrderCode(orderCode);
    }
}

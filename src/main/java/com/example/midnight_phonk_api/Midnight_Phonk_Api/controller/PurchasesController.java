package com.example.midnight_phonk_api.Midnight_Phonk_Api.controller;

import com.example.midnight_phonk_api.Midnight_Phonk_Api.model.Purchases;
import com.example.midnight_phonk_api.Midnight_Phonk_Api.service.PurchasesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchases")
public class PurchasesController {

    @Autowired
    private PurchasesService purchasesService;

    @GetMapping
    public ResponseEntity<List<Purchases>> getAllPurchases() {
        List<Purchases> purchases = purchasesService.getAllPurchases();
        return ResponseEntity.ok(purchases);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Purchases> getPurchaseById(@PathVariable Long id) {
        return purchasesService.getPurchaseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Purchases>> getPurchasesByUserId(@PathVariable Long userId) {
        List<Purchases> purchases = purchasesService.getPurchasesByUserId(userId);
        return ResponseEntity.ok(purchases);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Purchases>> getPurchasesByProductId(@PathVariable Long productId) {
        List<Purchases> purchases = purchasesService.getPurchasesByProductId(productId);
        return ResponseEntity.ok(purchases);
    }

    @PostMapping
    public ResponseEntity<Purchases> createPurchase(
            @RequestParam Long userId,
            @RequestParam Long productId,
            @RequestParam Integer quantity) {
        try {
            Purchases purchase = purchasesService.createPurchase(userId, productId, quantity);
            return ResponseEntity.ok(purchase);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchase(@PathVariable Long id) {
        return purchasesService.getPurchaseById(id)
                .map(purchase -> {
                    purchasesService.deletePurchase(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}

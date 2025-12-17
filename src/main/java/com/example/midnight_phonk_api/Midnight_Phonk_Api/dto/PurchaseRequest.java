package com.example.midnight_phonk_api.Midnight_Phonk_Api.dto;

import lombok.Data;
import java.util.List;

@Data
public class PurchaseRequest {
    private String userId; // This is the email
    private String userName;
    private Object shippingDetails; // Can be Map or String, we'll serialize it to String
    private List<PurchaseItem> items;
    private Double totalAmount;
    private String receiptPdfBase64;
    private String orderCode;

    @Data
    public static class PurchaseItem {
        private Long id;
        private String title;
        private Double price;
        private Integer quantity;
        private String image;
    }
}

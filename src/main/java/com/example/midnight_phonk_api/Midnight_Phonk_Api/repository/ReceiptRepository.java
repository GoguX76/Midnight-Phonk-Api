package com.example.midnight_phonk_api.Midnight_Phonk_Api.repository;

import com.example.midnight_phonk_api.Midnight_Phonk_Api.model.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    Optional<Receipt> findByOrderCode(String orderCode);
}

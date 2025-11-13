package com.example.midnight_phonk_api.Midnight_Phonk_Api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "products")
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotBlank
    @Column(nullable = false)
    public String title;

    @Column(nullable = false)
    public String image;

    @Column(nullable = false)
    public String shortDesc;

    @Column(nullable = false)
    public String fullDesc;

    @Column(nullable = false)
    public double price;
}

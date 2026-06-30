package com.example.E_Commerce.DTO;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto {


    private Long Id;

    //@NotNull for int and @NotBlank for String
    private String name;

    private Double price;

    private String description;

    private int stockQuantity;

    private boolean isAvailable;

    private int remainingQuantity;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

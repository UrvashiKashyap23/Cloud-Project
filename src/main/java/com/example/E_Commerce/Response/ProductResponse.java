package com.example.E_Commerce.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {


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

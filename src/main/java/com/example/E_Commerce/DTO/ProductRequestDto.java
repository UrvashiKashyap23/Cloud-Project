package com.example.E_Commerce.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDto {

    @NotBlank       //@NotNull for int and @NotBlank for String
    private String name;

    @NotNull
    private Double price;

    @NotBlank
    private String description;

    @Min(0)
    private int stockQuantity;

    @Min(0)
    private int filledStockQuantity;

    @NotNull
    private Long categoryId;
}

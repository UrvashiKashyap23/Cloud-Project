package com.example.E_Commerce.Response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {

    private Long categoryId;

    private String name;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

package com.example.E_Commerce.Response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseApiResponse<T> {

    private int code;

    private String message;

    private T data;
}
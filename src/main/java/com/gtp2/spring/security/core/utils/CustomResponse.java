package com.gtp2.spring.security.core.utils;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomResponse<T> {
    private String message;
    private int statusCode;
    private T data;

    public CustomResponse(String message, int statusCode, T data) {
        this.message = message;
        this.statusCode = statusCode;
        this.data = data;
    }
}

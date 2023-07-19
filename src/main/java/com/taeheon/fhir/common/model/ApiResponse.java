package com.taeheon.fhir.common.model;

import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private final String message;
    private final T data;

    private ApiResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> of(T data) {
        return new ApiResponse<>("success", data);
    }
}

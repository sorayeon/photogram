package com.cos.photogramstart.handler.ex;

import lombok.Getter;

import java.util.Map;

@Getter
public class CustomValidationApiException extends RuntimeException {

    private final Map<String, String> errorMap;

    public CustomValidationApiException(String message, Map<String, String> errorMap) {
        super(message);
        this.errorMap = errorMap;
    }
}

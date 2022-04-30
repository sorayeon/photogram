package com.cos.photogramstart.handler.ex;

import lombok.Getter;

import java.util.Map;

@Getter
public class CustomApiException extends RuntimeException {
    public CustomApiException(String message) {
        super(message);
    }
}

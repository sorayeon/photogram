package com.cos.photogramstart.handler.ex;

import lombok.Getter;

import java.util.Map;

@Getter
public class CustomException extends RuntimeException {

    public CustomException(String message) {
        super(message);
    }
}

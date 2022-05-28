package com.cos.photogramstart.handler.aop;

import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Component // RestController, Service 모든 것들이 Component 를 상속해서 만들어져 있음.
@Aspect
@Slf4j
public class ValidationAdvice {

    @Around("execution(* com.cos.photogramstart.web.api.*Controller.*(..))")
    public Object apiAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//        log.info("web api 컨트롤러 ==============");

        Object[] args = proceedingJoinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof BindingResult) {
//                log.info("유효성 검사를 하는 함수입니다.");
                BindingResult bindingResult = (BindingResult) arg;

                if(bindingResult.hasErrors()) {
                    Map<String, String> errorMap = new HashMap<>();
                    for (FieldError error : bindingResult.getFieldErrors()) {
                        errorMap.put(error.getField(), error.getDefaultMessage());
                        log.error(error.getDefaultMessage());
                    }
                    throw new CustomValidationApiException("유효성검증오류", errorMap);
                }
            }
        }

        // proceedingJoinPoint => profile 함수의 모든 곳에 접근할 수 있는 변수
        // profile 함수보다 먼저 실행
        return proceedingJoinPoint.proceed(); // profile 함수가 실행됨
    }

    @Around("execution(* com.cos.photogramstart.web.*Controller.*(..))")
    public Object advice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//        log.info("web 컨트롤러 ==============");

        Object[] args = proceedingJoinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof BindingResult) {
//                log.info("유효성 검사를 하는 함수입니다.");
                BindingResult bindingResult = (BindingResult) arg;

                if(bindingResult.hasErrors()) {
                    Map<String, String> errorMap = new HashMap<>();
                    for (FieldError error : bindingResult.getFieldErrors()) {
                        errorMap.put(error.getField(), error.getDefaultMessage());
                        log.error(error.getDefaultMessage());
                    }
                    throw new CustomValidationException("유효성검증오류", errorMap);
                }
            }
        }

        return proceedingJoinPoint.proceed();
    }
}

package com.example.nijo.common.exception;

import com.example.nijo.common.entity.RespEntity;
import com.example.nijo.common.enums.RespEnum;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 这里无法直接拦截AuthenticationException因为属于Filter中异常，所以写的很草率
     * 1.Security提供实现AuthenticationEntryPoint、AccessDeniedHandler来处理认证、授权异常
     *
     */
    @ExceptionHandler(Exception.class)
    public RespEntity authenticationExceptionHandler(Exception authenticationException) {
        if (authenticationException instanceof AuthenticationException) {
            RespEntity resp = new RespEntity(RespEnum.USERNAME_PASSWORD_ERROR);
            return resp;
        }
        return null;
    }
}

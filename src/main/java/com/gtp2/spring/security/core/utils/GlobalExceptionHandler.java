package com.gtp2.spring.security.core.utils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomResponse<String>> handleSecurityException(Exception exception) {
        CustomResponse<String> response = null;
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        if (exception instanceof BadCredentialsException) {
            status = HttpStatus.UNAUTHORIZED;
            response = new CustomResponse<>("The username or password is incorrect", status.value(), null);
        } else if (exception instanceof AccountStatusException) {
            status = HttpStatus.FORBIDDEN;
            response = new CustomResponse<>("The account is locked", status.value(), null);
        } else if (exception instanceof AccessDeniedException) {
            status = HttpStatus.FORBIDDEN;
            response = new CustomResponse<>("You are not authorized to access this resource", status.value(), null);
        } else if (exception instanceof SignatureException) {
            status = HttpStatus.FORBIDDEN;
            response = new CustomResponse<>("The JWT signature is invalid", status.value(), null);
        } else if (exception instanceof ExpiredJwtException) {
            status = HttpStatus.FORBIDDEN;
            response = new CustomResponse<>("The JWT token has expired", status.value(), null);
        } else {
            response = new CustomResponse<>("Unknown internal server error", status.value(), null);
        }

        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CustomResponse<String>> handleRuntimeException(RuntimeException ex) {
        CustomResponse<String> response = new CustomResponse<>(ex.getMessage(), HttpStatus.BAD_REQUEST.value(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomResponse<String>> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        CustomResponse<String> response = new CustomResponse<>(errorMessage, HttpStatus.BAD_REQUEST.value(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}

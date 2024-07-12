package com.locationguru.authentication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.locationguru.authentication.response.AuthResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AuthResponse> handleException(Exception ex) {
        AuthResponse response = new AuthResponse(500, "Internal server error: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<AuthResponse> handleIllegalStateException(IllegalStateException ex) {
        AuthResponse response = new AuthResponse(400, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<AuthResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        AuthResponse response = new AuthResponse(400, "Invalid parameter: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<AuthResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        AuthResponse response = new AuthResponse(400, "Invalid request body format: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}

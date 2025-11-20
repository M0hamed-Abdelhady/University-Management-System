package com.university.management.system.exceptions;

import com.university.management.system.dtos.ApiResponse;
import com.university.management.system.utils.ResponseEntityBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntityBuilder.create()
                .withStatus(HttpStatus.NOT_FOUND)
                .withMessage(ex.getMessage())
                .build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGlobalException(Exception ex) {
        return ResponseEntityBuilder.create()
                .withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .withMessage("An unexpected error occurred: " + ex.getMessage())
                .build();
    }
}

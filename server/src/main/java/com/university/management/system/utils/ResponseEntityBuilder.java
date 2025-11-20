package com.university.management.system.utils;

import com.university.management.system.dtos.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseEntityBuilder {
    private int status;
    private String message;
    private String error;
    private String path;
    private Map<String, Object> data;
    private HttpHeaders headers;

    private ResponseEntityBuilder() {
        this.data = new HashMap<>();
        this.headers = new HttpHeaders();
    }

    public static ResponseEntityBuilder create() {
        return new ResponseEntityBuilder();
    }

    public ResponseEntityBuilder withStatus(HttpStatus status) {
        this.status = status.value();
        return this;
    }

    public ResponseEntityBuilder withMessage(String message) {
        this.message = message;
        return this;
    }

    public ResponseEntityBuilder withError(String error) {
        this.error = error;
        return this;
    }

    public ResponseEntityBuilder withPath(String path) {
        this.path = path;
        return this;
    }

    public ResponseEntityBuilder withData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    public ResponseEntityBuilder withLocation(String location) {
        this.headers.add("Location", location);
        return this;
    }

    public ResponseEntity<ApiResponse> build() {
        ApiResponse response = ApiResponse.builder()
                .status(status)
                .message(message)
                .error(error)
                .path(path)
                .data(data.isEmpty() ? null : data)
                .build();
        return new ResponseEntity<>(response, headers, HttpStatus.valueOf(status));
    }
}

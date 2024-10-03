package com.example.springtutorialapp.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ErrorResponse {
    private HttpStatus httpCode;
    private int httpStatus;
    private String message;

    public ErrorResponse(String message) {
        super();
        this.message = message;
    }
}

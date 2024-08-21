package com.gonzalojerez.dev.permissionsapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,Object> handleBadRequests(BadRequestException exception, WebRequest req){
        Map<String,Object> response = new HashMap<>();

        response.put("error", exception.getMessage());
        response.put("status", HttpStatus.BAD_REQUEST.value());

        return response;
    }
}

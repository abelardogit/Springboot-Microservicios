package com.paymentchain.customer.common;

import org.apache.coyote.Response;
import org.apache.http.protocol.HTTP;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandarizedApiExceptionResponse> handlerUnknownHostException(Exception exc) {
        String[] params = {"TECNICO", "I/O Error", "1024", exc.getMessage(), ""};
        StandarizedApiExceptionResponse standarizedApiExceptionResponse = StandarizedApiExceptionResponse.create(params);
        return ResponseEntity.internalServerError().body(standarizedApiExceptionResponse);
    }
}

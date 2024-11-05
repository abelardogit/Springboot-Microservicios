package com.paymentchain.customer.exceptions;

import com.paymentchain.customer.common.StandarizedApiExceptionResponse;
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

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<StandarizedApiExceptionResponse> handlerUnknownHostException(BusinessRuleException bre) {
        String[] params = {"NEGOCIO", "Precondition failed", bre.getCode(), bre.getMessage(), ""};
        StandarizedApiExceptionResponse standarizedApiExceptionResponse = StandarizedApiExceptionResponse.create(params);
        return new ResponseEntity<>(standarizedApiExceptionResponse, bre.getHttpStatus());
    }
}

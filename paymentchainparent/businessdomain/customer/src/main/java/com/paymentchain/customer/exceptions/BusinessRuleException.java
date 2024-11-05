package com.paymentchain.customer.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = false)
public class BusinessRuleException extends Exception {
    private long id;
    private String code;
    private HttpStatus httpStatus;

    private BusinessRuleException(long id, String code, String message, HttpStatus httpStatus) {
        super(message);
        this.id = id;
        this.code = code;
        this.httpStatus = httpStatus;
    }

    private BusinessRuleException(String code, String message, HttpStatus httpStatus) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
    }

    private BusinessRuleException(String message, Throwable cause) {
        super(message, cause);
    }

    public static BusinessRuleException fromCreate(Object[] params) {
        long id = (Long) params[0];
        String code = (String) params[1];
        String message = (String) params[2];
        HttpStatus httpStatus = (HttpStatus) params[3];

        return new BusinessRuleException(id, code, message, httpStatus);
    }

    public static BusinessRuleException fromCode(Object[] params) {
        String code = (String) params[0];
        String message = (String) params[1];
        HttpStatus httpStatus = (HttpStatus) params[2];

        return new BusinessRuleException(code, message, httpStatus);
    }

    public static BusinessRuleException fromThrowable(Object[] params) {
        String message = (String) params[0];
        Throwable cause = (Throwable) params[1];

        return new BusinessRuleException(message, cause);
    }

}

package com.paymentchain.customer.common;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class StandarizedApiExceptionResponse {
    private String type;
    private String title;
    private String code;
    private String detail;
    private String instance;

    private StandarizedApiExceptionResponse(String type, String title, String code, String detail, String instance) {
        this.type = type;
        this.title = title;
        this.code = code;
        this.detail = detail;
        this.instance = instance;
    }

    public static StandarizedApiExceptionResponse create(String[] params) {
        return new StandarizedApiExceptionResponse(params[0], params[1], params[2], params[3], params[4]);
    }
}

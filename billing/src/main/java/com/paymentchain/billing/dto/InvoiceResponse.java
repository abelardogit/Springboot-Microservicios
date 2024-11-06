package com.paymentchain.billing.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "InvoiceResponse", description = "A invoice")
public class InvoiceResponse {
    private long id;
    @Schema(name = "CustomerID", requiredMode = Schema.RequiredMode.REQUIRED, example = "12", defaultValue = "1", description = "A customer ID")
    private long customerId;
    @Schema(name = "number", requiredMode = Schema.RequiredMode.REQUIRED, example = "3", defaultValue = "2", description = "A number of invoice")
    private String number;
    private String detail;
    private double amount;
}

package com.paymentchain.billing.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "InvoiceRequest", description = "A invoice")
@Data
public class InvoiceRequest {
    @Schema(name = "customerID", requiredMode = Schema.RequiredMode.REQUIRED, example = "12", defaultValue = "1", description = "A customer ID")
    private long customerID;
    @Schema(name = "number", requiredMode = Schema.RequiredMode.REQUIRED, example = "3", defaultValue = "2", description = "A number of invoice")
    private String number;
    private String detail;
    private double amount;
}

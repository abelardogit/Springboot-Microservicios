package com.paymentchain.billing.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.lang.annotation.ElementType;

@Entity
@Data
@Schema(name = "Invoice", description = "A invoice")
public class Invoice {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;
    @Schema(name = "CustomerID", requiredMode = Schema.RequiredMode.REQUIRED, example = "12", defaultValue = "1", description = "A customer ID")
    private long customerId;
    @Schema(name = "number", requiredMode = Schema.RequiredMode.REQUIRED, example = "3", defaultValue = "2", description = "A number of invoice")
    private String number;
    private String detail;
    private double amount;
}
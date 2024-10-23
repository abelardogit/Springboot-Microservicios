package com.paymentchain.customer.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CustomerProduct {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;
    private long productId;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Customer.class)
    @JoinColumn(name="customerId", nullable = true)
    private Customer customer;
    @Transient
    private String productName;

    @Override
    public String toString() {
        return "id:" + id + " productId:" + productId;
    }
}

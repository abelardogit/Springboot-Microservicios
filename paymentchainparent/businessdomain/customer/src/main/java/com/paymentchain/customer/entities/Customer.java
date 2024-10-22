package com.paymentchain.customer.entities;

import java.util.List;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Customer {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;
    private String code;
    private String iban;
    private String names;
    private String surname;
    private String phone;
    private String address;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerProduct> products;
    @Transient
    private List<?> transactions;

}
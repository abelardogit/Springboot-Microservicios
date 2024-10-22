package com.paymentchain.customer.entities;

import java.util.List;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Customer {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;

    @NotBlank(message="Code is mandatory")
    private String code;
    @NotBlank(message="IBAN is mandatory")
    private String iban;
    @NotBlank(message="Name is mandatory")
    private String name;
    @NotBlank(message="Surname is mandatory")
    private String surname;
    @NotBlank(message="Phone is mandatory")
    private String phone;
    @NotBlank(message="Address is mandatory")
    private String address;

    @NotNull(message="Products is mandatory")
    @NotEmpty
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerProduct> products;

    @NotNull(message="Transactions is mandatory")
    @Transient
    private List<?> transactions;

}
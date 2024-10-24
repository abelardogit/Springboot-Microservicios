package com.paymentchain.transaction.entities;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;



@Entity
@Data
public class Transaction {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;
    private String reference;
    private String iban;
    private Date date;
    private double amount;
    private double fee;
    private String description;
    private String status;
    private String channel;
}
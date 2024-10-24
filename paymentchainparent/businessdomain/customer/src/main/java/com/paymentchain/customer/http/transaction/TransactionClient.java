package com.paymentchain.customer.http.transaction;

import java.util.List;

public interface TransactionClient {
    List<?> getTransactionByCustomerIban(String iban);
}

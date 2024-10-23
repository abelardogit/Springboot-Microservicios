package com.paymentchain.customer.http;

import reactor.core.publisher.Flux;

public interface ProductClient {
    String getProductName(long productId);
}

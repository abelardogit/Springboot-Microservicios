package com.paymentchain.customer;

import com.paymentchain.customer.http.product.ProductClient;
import com.paymentchain.customer.http.product.WebClientProductClient;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class CustomerConfiguration {
    @Bean
    @Profile("sse")
    public ProductClient webClientProductClient(WebClient webClient) {
        return new WebClientProductClient(webClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}

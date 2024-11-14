package com.paymentchain.customer;

import com.paymentchain.customer.http.product.ProductClient;
import com.paymentchain.customer.http.product.WebClientProductClient;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class CustomerConfiguration {

     @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        System.out.println(WebClient.builder());
        return WebClient.builder();
    }
    
    @Bean
    @Profile("sse")
    public ProductClient webClientProductClient() {
        return new WebClientProductClient(loadBalancedWebClientBuilder());
    }


    @Bean
    @ConditionalOnMissingBean
    public WebClient webClient() {
        return WebClient.builder().build();
    }

}

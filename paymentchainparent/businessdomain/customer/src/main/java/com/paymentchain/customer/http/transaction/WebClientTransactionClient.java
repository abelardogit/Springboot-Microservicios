/**
 * @see https://blog.jetbrains.com/idea/2019/11/tutorial-reactive-spring-boot-a-rest-client-for-reactive-streams/
 **/

package com.paymentchain.customer.http.transaction;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.List;

import static java.time.Duration.ofSeconds;
import static reactor.util.retry.Retry.backoff;

@Log4j2
public class WebClientTransactionClient implements TransactionClient {
    public static final String URL_TRANSACTION = "http://localhost:8082/transaction/";
    public static final String TRANSACTION_IBAN_SERVICE = "iban";

    private final WebClient webClient;

    public WebClientTransactionClient(WebClient webClient) {
        this.webClient = webClient;
    }

    private Flux<Object> getTransactions(String iban) {
        log.info("WebClient product client");
        String url = WebClientTransactionClient.URL_TRANSACTION + WebClientTransactionClient.TRANSACTION_IBAN_SERVICE + "/{iban}";
        return this.webClient.get()
                .uri(url, iban)
                .retrieve()
                .bodyToFlux(Object.class)
                .retryWhen(backoff(5, ofSeconds(1)).maxBackoff(ofSeconds(3)))
                .doOnError(IOException.class, e -> log.error(e.getMessage()))
        ;
    }

    @Override
    public List<?> getTransactionByCustomerIban(String iban) {
        Flux<Object> transactionsFlux = this.getTransactions(iban);
        List<?> transactions = transactionsFlux.collectList().block();
        return transactions;
    }
}

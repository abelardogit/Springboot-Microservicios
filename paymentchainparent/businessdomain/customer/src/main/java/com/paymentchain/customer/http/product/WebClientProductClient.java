/**
 * @see https://blog.jetbrains.com/idea/2019/11/tutorial-reactive-spring-boot-a-rest-client-for-reactive-streams/
 **/

package com.paymentchain.customer.http.product;

import java.io.IOException;
import static java.time.Duration.ofSeconds;

import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.Optional;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import static reactor.util.retry.Retry.backoff;

@Log4j2
public class WebClientProductClient implements ProductClient {
    public static final String URL_PRODUCT = "http://localhost:8081/product";
    public static final String PRODUCT_GETNAME_SERVICE = "/getName";

    private final WebClient webClient;

    public WebClientProductClient(WebClient webClient) {
        this.webClient = webClient;
    }

    private Flux<String> productName(long productId) {
        log.info("WebClient product client");
        String url = WebClientProductClient.URL_PRODUCT + WebClientProductClient.PRODUCT_GETNAME_SERVICE + "/{id}";
      return this.webClient.get()
            .uri(url, productId)
            .retrieve()
            .bodyToFlux(String.class)
            .retryWhen(backoff(3, ofSeconds(2))
                .maxBackoff(ofSeconds(10))
                .jitter(0.5)
                .filter(throwable -> 
                    !(throwable instanceof WebClientResponseException) || 
                    ((WebClientResponseException) throwable).getStatusCode().is5xxServerError())
            )
            .doOnError(IOException.class, e -> log.error("Error processing request: " + e.getMessage()));
    }

    @Override
    public String getProductName(long id) {
        Flux<String> aFluxNameProduct = this.productName(id);
        return Optional.ofNullable(aFluxNameProduct.blockFirst()).orElse("Nombre de producto no encontrado");

       // return Objects.requireNonNull(aFluxNameProduct.blockFirst());
    }

    public String getNotFoundProductName(long id, boolean is404) throws UnknownHostException {
        if (is404) {
           return "";
        }
        throw new UnknownHostException("Not product name with id " + id + " was found");
    }
}

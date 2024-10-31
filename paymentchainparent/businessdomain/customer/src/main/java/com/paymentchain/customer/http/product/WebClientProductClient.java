/**
 * @see https://blog.jetbrains.com/idea/2019/11/tutorial-reactive-spring-boot-a-rest-client-for-reactive-streams/
 **/

package com.paymentchain.customer.http.product;

import java.io.IOException;
import static java.time.Duration.ofSeconds;
import java.util.Objects;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import static reactor.util.retry.Retry.backoff;

@Log4j2
public class WebClientProductClient implements ProductClient {
    public static final String URL_PRODUCT = "http://BUSINESSDOMAIN-PRODUCT/product";
    public static final String PRODUCT_GETNAME_SERVICE = "getName";

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
                .retryWhen(backoff(5, ofSeconds(1)).maxBackoff(ofSeconds(3)))
                .doOnError(IOException.class, e -> log.error(e.getMessage()))
        ;
    }

    @Override
    public String getProductName(long id) {
        Flux<String> aFluxNameProduct = this.productName(id);
        return Objects.requireNonNull(aFluxNameProduct.blockFirst());
    }
}

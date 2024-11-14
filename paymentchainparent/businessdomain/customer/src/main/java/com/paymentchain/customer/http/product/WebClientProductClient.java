/**
 * @see https://blog.jetbrains.com/idea/2019/11/tutorial-reactive-spring-boot-a-rest-client-for-reactive-streams/
 **/

package com.paymentchain.customer.http.product;

import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.io.IOException;
import static java.time.Duration.ofSeconds;

import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.netty.http.client.HttpClient;
import static reactor.util.retry.Retry.backoff;

@Log4j2
public class WebClientProductClient implements ProductClient {
    public static final String URL_PRODUCT = "http://BUSINESSDOMAIN-PRODUCT/product";
    public static final String PRODUCT_GETNAME_SERVICE = "/getName";

    private final WebClient.Builder webClient;

   
    public WebClientProductClient(WebClient.Builder webClient) {
        this.webClient = webClient;
    }

    private HttpClient createHttpClient() {
        return HttpClient.create()
                // Connection Timeout
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(EpollChannelOption.TCP_KEEPIDLE, 300)
                .option(EpollChannelOption.TCP_KEEPINTVL, 60)
                // Response Timeout
                .responseTimeout(Duration.ofSeconds(1))
                // Read and Write Timeout
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));
                });
    }

    private Flux<String> productName(long productId) {
        log.info("WebClient product client");
        String url = WebClientProductClient.URL_PRODUCT + WebClientProductClient.PRODUCT_GETNAME_SERVICE + "/{id}";

        // Crear el WebClient con el HttpClient personalizado
        WebClient customWebClient = this.webClient
                .clientConnector(new ReactorClientHttpConnector(createHttpClient()))
                .build(); // Finalizar la construcción del WebClient

        // Realizar la solicitud con reintentos y manejo de errores
        return customWebClient.get()
                .uri(url, productId)
                .retrieve()
                .bodyToFlux(String.class)
                .retryWhen(backoff(3, ofSeconds(2))
                        .maxBackoff(ofSeconds(10))
                        .jitter(0.5)
                        .filter(throwable -> !(throwable instanceof WebClientResponseException) ||
                                ((WebClientResponseException) throwable).getStatusCode().is5xxServerError()))
                .doOnError(IOException.class, e -> log.error("Error processing request: " + e.getMessage()))
                .doOnError(throwable -> log.error("Unexpected error: {}", throwable.getMessage()));
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

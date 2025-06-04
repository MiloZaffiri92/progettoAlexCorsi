package org.example.academycorsi.service;

import lombok.extern.slf4j.Slf4j;
import org.example.academycorsi.data.dto.DiscenteDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;


@Service
@Slf4j
public class DiscenteWebClientService {


    private final WebClient webClient;
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(5);
    private static final int MAX_RETRY_ATTEMPTS = 3;
    private static final Duration RETRY_DELAY = Duration.ofSeconds(1);

    public DiscenteWebClientService(@Value("${discenti.service.url}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }



    public DiscenteDTO getDiscenteById(Long id) {
        return webClient.get()
                .uri("/discenti/{id}", id)
                .retrieve()
                .bodyToMono(DiscenteDTO.class)
                .retryWhen(Retry.fixedDelay(MAX_RETRY_ATTEMPTS, RETRY_DELAY))
                .block(REQUEST_TIMEOUT);

    }

//
//    public Mono<DiscenteDTO> saveDiscente(DiscenteDTO discenteDTO) {
//        return webClient.post()
//                .uri("/discenti")
//                .bodyValue(discenteDTO)
//                .retrieve()
//                .bodyToMono(DiscenteDTO.class)
//                .retryWhen(Retry.fixedDelay(MAX_RETRY_ATTEMPTS, RETRY_DELAY))
//                .doOnSubscribe(subscription ->
//                        log.info("Salvataggio nuovo discente iniziato"))
//                .doOnSuccess(discente ->
//                        log.info("Nuovo discente salvato con successo"))
//                .doOnError(error ->
//                        log.error("Errore nel salvataggio del discente: {}", error.getMessage()));
//    }




}

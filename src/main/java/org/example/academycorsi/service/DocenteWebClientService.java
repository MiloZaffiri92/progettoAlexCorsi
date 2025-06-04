package org.example.academycorsi.service;

import lombok.extern.slf4j.Slf4j;
import org.example.academycorsi.data.dto.DocenteDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class DocenteWebClientService {


    private final WebClient webClientDocenti;

    public DocenteWebClientService(@Value("${docenti.service.url}") String baseUrl) {
        this.webClientDocenti = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }


    public Mono<DocenteDTO> getDocenteById(Long id) {
        return webClientDocenti.get()
                .uri("/docenti/{id}", id)  // Aggiornato per corrispondere all'endpoint corretto
                .retrieve()
                .bodyToMono(DocenteDTO.class)
                .doOnSubscribe(subscription ->
                        log.info("Iniziata richiesta per docente con ID: {}", id))
                .doOnSuccess(docente ->
                        log.info("Docente recuperato con successo: {} {}", docente.getNome(), docente.getCognome()))
                .doOnError(error ->
                        log.error("CIAO ERRORE ARRIVATO: Errore nel recupero del docente con ID {}: {}", id, error.getMessage()));
    }



}

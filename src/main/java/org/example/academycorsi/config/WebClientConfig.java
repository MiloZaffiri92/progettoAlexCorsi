package org.example.academycorsi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${docenti.service.url}")
    private String docentiServiceUrl;

    @Bean
    public WebClient webClientDocenti() {
        return WebClient.builder()
                .baseUrl(docentiServiceUrl)
                .build();
    }


}

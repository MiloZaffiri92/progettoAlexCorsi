package org.example.academycorsi.service;

import org.example.academycorsi.config.RestTemplateConfig;
import org.example.academycorsi.data.dto.DocenteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocenteRestTemplateService {

    @Autowired
    private RestTemplateConfig restTemplateConfig;


    private final String urlDocenti = "http://localhost:8080";

//public List<DocenteDTO> getAllDocenti() {
//    ResponseEntity<List<DocenteDTO>> response = restTemplateConfig.restTemplate().exchange(
//            urlDocenti + "/docenti",
//            HttpMethod.GET,
//            null,
//            new ParameterizedTypeReference<List<DocenteDTO>>() {}
//    );
//    return response.getBody();
//}



}

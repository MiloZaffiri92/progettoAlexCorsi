package org.example.academycorsi.service;
import jakarta.transaction.Transactional;
import org.example.academycorsi.converter.CorsoMapper;
import org.example.academycorsi.data.dto.CorsoDTO;
import org.example.academycorsi.data.dto.DiscenteDTO;
import org.example.academycorsi.data.dto.DocenteDTO;
import org.example.academycorsi.data.entity.Corso;
import org.example.academycorsi.repository.CorsoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CorsoService {


    @Autowired
    private CorsoRepository corsoRepository;
    @Autowired
    private CorsoMapper corsoMapper;
    @Autowired
    private RestTemplate restTemplate;



    private final String urlDocenti = "http://localhost:8080/docenti/{docenteId}";
    private final String urlDiscenti = "http://localhost:8080/discenti/corso/{corsoId}";


    private DocenteDTO getDocente(Long docenteId) {
        if (docenteId == null) {
            return new DocenteDTO("Non disponibile", "Non disponibile");
        }
        try {
            ResponseEntity<DocenteDTO> response = restTemplate.getForEntity(
                    urlDocenti,
                    DocenteDTO.class,
                    docenteId
            );
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                DocenteDTO docente = response.getBody();
                return new DocenteDTO(docente.getNome(), docente.getCognome());
            }
        } catch (Exception e) {
            System.out.println("Errore nel recupero del docente con ID " + docenteId + ": " + e.getMessage());
        }
        return new DocenteDTO("Non disponibile", "Non disponibile");
    }

    public List<CorsoDTO> findAll() {
        return corsoRepository.findAll().stream()
                .map(corso -> {
                    CorsoDTO dto = corsoMapper.corsoToDto(corso);
                    dto.setDocente(getDocente(corso.getDocenteId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }




    public CorsoDTO save(CorsoDTO corsoDTO) {
        if (corsoDTO.getNome() == null || corsoDTO.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome del corso è obbligatorio");
        }

        Corso corso = new Corso();
        corso.setNome(corsoDTO.getNome());
        corso.setAnnoAccademico(corsoDTO.getAnnoAccademico());
        corso.setDocenteId(corsoDTO.getDocenteId());

        Corso savedCorso = corsoRepository.save(corso);
        return corsoMapper.corsoToDto(savedCorso);
    }




    public void deleteById(Long id) {
        corsoRepository.deleteById(id);
    }


    public CorsoDTO updateCorso(Long id, CorsoDTO corso) {
        Corso updateCorso = corsoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Corso non trovato"));

        if(corso.getNome() != null) updateCorso.setNome(corso.getNome());
        if(corso.getAnnoAccademico() != null) updateCorso.setAnnoAccademico(corso.getAnnoAccademico());

        Corso saved = corsoRepository.save(updateCorso);
        return corsoMapper.corsoToDto(saved);
    }
}

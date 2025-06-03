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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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



    private final String urlDocenti = "http://localhost:8080/docenti";
    private final String urlDiscenti = "http://localhost:8080/discenti/corso/{corsoId}";



    private DocenteDTO getDocente(Long docenteId) {
        try {
            return restTemplate.getForObject(
                    urlDocenti,
                    DocenteDTO.class,
                    docenteId
            );
        } catch (Exception e) {
            return new DocenteDTO("Non disponibile", "Non disponibile");
        }
    }

    private List<DiscenteDTO> getDiscenti(Long corsoId) {
        try {
            ResponseEntity<List<DiscenteDTO>> response = restTemplate.exchange(
                    urlDiscenti,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<DiscenteDTO>>() {},
                    corsoId
            );
            return response.getBody();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<CorsoDTO> findAll() {
        return corsoRepository.findAll().stream()
                .map(corso -> {
                    CorsoDTO dto = corsoMapper.corsoToDto(corso);
                    dto.setDocente(getDocente(corso.getDocenteId()));
                    dto.setDiscenti(getDiscenti(corso.getId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }




    public CorsoDTO save(CorsoDTO corsoDTO){
        Corso corso = corsoMapper.corsoToEntity(corsoDTO);

        corso.setNome(corsoDTO.getNome());
        corso.setAnnoAccademico(corsoDTO.getAnnoAccademico());

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

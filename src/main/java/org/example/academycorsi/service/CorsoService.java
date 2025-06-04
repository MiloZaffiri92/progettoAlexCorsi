package org.example.academycorsi.service;
import lombok.RequiredArgsConstructor;
import org.example.academycorsi.config.DocentiFeignClient;
import org.example.academycorsi.converter.CorsoMapper;
import org.example.academycorsi.data.dto.CorsoDTO;
import org.example.academycorsi.data.dto.DocenteDTO;
import org.example.academycorsi.data.entity.Corso;
import org.example.academycorsi.repository.CorsoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CorsoService {


    @Autowired
    private CorsoRepository corsoRepository;
    @Autowired
    private CorsoMapper corsoMapper;

    private final DocentiFeignClient docentiFeignClient;

    public List<CorsoDTO> findAll() {
        return corsoRepository.findAll().stream()
                .map(corso -> {
                    CorsoDTO dto = corsoMapper.corsoToDto(corso);
                    if (corso.getDocenteId() != null) {
                        try {
                            dto.setDocente(docentiFeignClient.getDocenteById(corso.getDocenteId()).getBody());
                        } catch (Exception e) {
                            // Log dell'errore ma continua l'esecuzione
                            // Il docente rimarrà null nel DTO
                        }
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }


    public CorsoDTO save(CorsoDTO corsoDTO) {
        // Verifica l'esistenza del docente se specificato
        if (corsoDTO.getDocenteId() != null) {
            try {
                docentiFeignClient.getDocenteById(corsoDTO.getDocenteId());
            } catch (Exception e) {
                throw new RuntimeException("Docente non trovato con ID: " + corsoDTO.getDocenteId());
            }
        }

        Corso corso = corsoMapper.corsoToEntity(corsoDTO);
        Corso savedCorso = corsoRepository.save(corso);
        return corsoWithDocenteInfo(corsoMapper.corsoToDto(savedCorso));
    }


    public void deleteById(Long id) {
        corsoRepository.deleteById(id);
    }


    public CorsoDTO updateCorso(Long id, CorsoDTO corso) {
        Corso updateCorso = corsoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Corso non trovato"));

        if (corso.getNome() != null) updateCorso.setNome(corso.getNome());
        if (corso.getAnnoAccademico() != null) updateCorso.setAnnoAccademico(corso.getAnnoAccademico());

        Corso saved = corsoRepository.save(updateCorso);
        return corsoMapper.corsoToDto(saved);
    }

    // Metodo helper per arricchire il DTO con le informazioni del docente
    private CorsoDTO corsoWithDocenteInfo(CorsoDTO corsoDTO) {
        if (corsoDTO.getDocenteId() != null) {
            try {
                DocenteDTO docente = docentiFeignClient.getDocenteById(corsoDTO.getDocenteId()).getBody();
                corsoDTO.setDocente(docente);
            } catch (Exception e) {
                // Il docente rimarrà null nel DTO
            }
        }
        return corsoDTO;
    }

}

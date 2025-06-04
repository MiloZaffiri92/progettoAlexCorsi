package org.example.academycorsi.service;
import lombok.extern.slf4j.Slf4j;
import org.example.academycorsi.converter.CorsoMapper;
import org.example.academycorsi.data.dto.CorsoDTO;
import org.example.academycorsi.data.dto.DocenteDTO;
import org.example.academycorsi.data.entity.Corso;
import org.example.academycorsi.repository.CorsoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CorsoService {


    @Autowired
    private CorsoRepository corsoRepository;
    @Autowired
    private CorsoMapper corsoMapper;

    @Autowired
    private DocenteWebClientService docenteService;


    private CorsoDTO moreInfoDocente(CorsoDTO dto) {
        if (dto.getDocenteId() != null) {
            try {
                DocenteDTO docente = docenteService.getDocenteById(dto.getDocenteId())
                        .timeout(Duration.ofSeconds(5))
                        .block();
                if (docente != null) {
                    dto.setDocente(docente);
                } else {
                    log.warn("Docente non trovato per ID: {}", dto.getDocenteId());
                }
            } catch (Exception e) {
                log.error("Errore nel recupero del docente con ID: {}", dto.getDocenteId(), e);
                // Non propagare l'eccezione, permettendo all'applicazione di continuare
            }
        }
        return dto;
    }

    public List<CorsoDTO> findAll() {
        return corsoRepository.findAll().stream()
                .map(corso -> {
                    CorsoDTO dto = corsoMapper.corsoToDto(corso);
                    return moreInfoDocente(dto);
                })
                .collect(Collectors.toList());
    }



    public CorsoDTO save(CorsoDTO corsoDTO) {
        // Verifica l'esistenza del docente se è stato specificato un docenteId
        if (corsoDTO.getDocenteId() != null) {
            try {
                DocenteDTO docente = docenteService.getDocenteById(corsoDTO.getDocenteId())
                        .timeout(Duration.ofSeconds(5))
                        .block();

                if (docente == null) {
                    throw new RuntimeException("Il docente con ID " + corsoDTO.getDocenteId() + " non esiste");
                }

                // Impostiamo il docente nel DTO per avere le informazioni complete
                corsoDTO.setDocente(docente);
            } catch (Exception e) {
                throw new RuntimeException("Errore nella verifica del docente: " + e.getMessage());
            }
        }

        Corso corso = corsoMapper.corsoToEntity(corsoDTO);
        corso.setNome(corsoDTO.getNome());
        corso.setAnnoAccademico(corsoDTO.getAnnoAccademico());
        corso.setDocenteId(corsoDTO.getDocenteId());

        Corso savedCorso = corsoRepository.save(corso);
        CorsoDTO savedDto = corsoMapper.corsoToDto(savedCorso);

        // Aggiungiamo le informazioni del docente al DTO restituito
        if (corsoDTO.getDocente() != null) {
            savedDto.setDocente(corsoDTO.getDocente());
        }

        return savedDto;
    }


    public void deleteById(Long id) {
        corsoRepository.deleteById(id);
    }


    public CorsoDTO updateCorso(Long id, CorsoDTO corsoDTO) {
        Corso corsoEsistente = corsoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Corso non trovato con ID: " + id));

        // Aggiorna i campi base solo se sono presenti nel DTO
        if (corsoDTO.getNome() != null) {
            corsoEsistente.setNome(corsoDTO.getNome());
        }
        if (corsoDTO.getAnnoAccademico() != null) {
            corsoEsistente.setAnnoAccademico(corsoDTO.getAnnoAccademico());
        }

        // Gestione del docenteId
        if (corsoDTO.getDocenteId() != null) {
            try {
                DocenteDTO docente = docenteService.getDocenteById(corsoDTO.getDocenteId())
                        .timeout(Duration.ofSeconds(5))
                        .block();

                if (docente == null) {
                    throw new RuntimeException("Il docente con ID " + corsoDTO.getDocenteId() + " non esiste");
                }

                corsoEsistente.setDocenteId(corsoDTO.getDocenteId());
                corsoDTO.setDocente(docente);
            } catch (Exception e) {
                throw new RuntimeException("Errore nella verifica del docente: " + e.getMessage());
            }
        }

        Corso savedCorso = corsoRepository.save(corsoEsistente);
        CorsoDTO savedDto = corsoMapper.corsoToDto(savedCorso);

        // Aggiungiamo le informazioni del docente al DTO restituito
        if (corsoDTO.getDocente() != null) {
            savedDto.setDocente(corsoDTO.getDocente());
        }

        return savedDto;
    }

}

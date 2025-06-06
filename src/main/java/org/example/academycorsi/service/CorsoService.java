package org.example.academycorsi.service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.academycorsi.converter.CorsoMapper;
import org.example.academycorsi.data.dto.CorsoDTO;
import org.example.academycorsi.data.dto.DiscenteDTO;
import org.example.academycorsi.data.dto.DocenteDTO;
import org.example.academycorsi.data.entity.Corso;
import org.example.academycorsi.data.entity.CorsoDiscenti;
import org.example.academycorsi.repository.CorsoDiscentiRepository;
import org.example.academycorsi.repository.CorsoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CorsoService {
    private final CorsoRepository corsoRepository;
    private final CorsoMapper corsoMapper;
    private final DocenteWebClientService docenteWebClientService;
    private final CorsoDiscentiRepository corsoDiscentiRepository;
    private final DiscenteWebClientService discenteWebClientService;


    private CorsoDTO moreInfoDocente(CorsoDTO dto) {
        if (dto.getDocenteId() != null) {
            try {
                DocenteDTO docente = docenteWebClientService.getDocenteById(dto.getDocenteId())
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

    private CorsoDTO moreInfoDiscenti(CorsoDTO dto) {
        try {
            List<Long> idDiscenti = corsoDiscentiRepository.findIdsDiscenteByIdCorso(
                    corsoRepository.findIdByNome(dto.getNome())
            );
            Set<DiscenteDTO> discentiInfo = new HashSet<>();

            for (Long idDiscente : idDiscenti) {
                try {
                    DiscenteDTO discente = discenteWebClientService.getDiscenteById(idDiscente);

                    if (discente != null) {
                        discentiInfo.add(discente);
                        log.info("Aggiunto discente: {}", discente);
                    } else {
                        log.warn("Nessun discente trovato per ID: {}", idDiscente);
                    }
                } catch (Exception e) {
                    log.error("Errore nel recupero del discente con ID: {}", idDiscente, e);
                }
            }

            if (!discentiInfo.isEmpty()) {
                dto.setDiscenti(new ArrayList<>(discentiInfo));
            }
        } catch (Exception e) {
            log.error("Errore nel recupero dei discenti per il corso: {}", dto.getNome(), e);
        }
        return dto;
    }



    public List<CorsoDTO> findAll() {
        return corsoRepository.findAll().stream()
                .map(corso -> {
                    CorsoDTO dto = corsoMapper.corsoToDto(corso);
                    dto = moreInfoDocente(dto);
                    return moreInfoDiscenti(dto);
                })
                .collect(Collectors.toList());
    }




    @Transactional
    public CorsoDTO save(CorsoDTO corsoDTO) {
        Corso corso = corsoMapper.corsoToEntity(corsoDTO);
        corso.setDocenteId(corsoDTO.getDocenteId());
        Corso savedCorso = corsoRepository.save(corso);

        // Chiamiamo salvaDiscenti con il DTO completo
        saveDiscenti(corsoDTO, savedCorso.getId());

        return corsoMapper.corsoToDto(savedCorso);
    }


    private void saveDiscenti(CorsoDTO corsoDTO, Long idCorso) {
        if (corsoDTO.getDiscenti() == null) {
            return;
        }

        // Prima eliminiamo tutte le associazioni esistenti per questo corso
        corsoDiscentiRepository.deleteByCorsoId(idCorso);

        // Poi creiamo le nuove associazioni
        corsoDTO.getDiscenti().forEach(discenteDTO -> {
            try {
                // Otteniamo l'ID del discente usando nome e cognome
                Long discenteId = discenteWebClientService.getDiscenteIdByNomeAndCognome(
                        discenteDTO.getNome(),
                        discenteDTO.getCognome()
                );

                if (discenteId != null) {
                    CorsoDiscenti corsoDiscenti = new CorsoDiscenti();
                    corsoDiscenti.setCorsoId(idCorso);
                    corsoDiscenti.setDiscenteId(discenteId);
                    corsoDiscentiRepository.save(corsoDiscenti);
                    log.info("Salvata associazione corso-discente: Corso ID {}, Discente ID {}",
                            idCorso, discenteId);
                } else {
                    log.warn("Discente non trovato con nome: {} e cognome: {}",
                            discenteDTO.getNome(), discenteDTO.getCognome());
                }
            } catch (Exception e) {
                log.error("Errore nel salvare l'associazione corso-discente per discente {}: {}",
                        discenteDTO.getNome() + " " + discenteDTO.getCognome(), e.getMessage());
            }
        });
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
                DocenteDTO docente = docenteWebClientService.getDocenteById(corsoDTO.getDocenteId())
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

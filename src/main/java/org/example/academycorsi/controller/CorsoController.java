package org.example.academycorsi.controller;

import org.example.academycorsi.data.dto.CorsoDTO;
import org.example.academycorsi.service.CorsoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/corsi")
public class CorsoController {


    @Autowired
    private CorsoService corsoService;

    @GetMapping("/list")
    public List<CorsoDTO> list() {
        return corsoService.findAll();
    }

    @PostMapping("/nuovo")
    public ResponseEntity<?> salvaCorso(@RequestBody CorsoDTO corsoDTO) {
        try {
            if (corsoDTO == null) {
                return ResponseEntity
                        .badRequest()
                        .body("Il corpo della richiesta non può essere vuoto");
            }

            CorsoDTO saved = corsoService.save(corsoDTO);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore durante il salvataggio del corso: " + e.getMessage());
        }
    }



    @PutMapping("/{id}")
    public ResponseEntity<CorsoDTO> modificaCorso(@PathVariable Long id,
                                                  @RequestBody CorsoDTO corso) {
        CorsoDTO corsoDTO = corsoService.updateCorso(id, corso);
        return ResponseEntity.ok(corsoDTO);
    }

//    @DeleteMapping("/{id}")
//    public void eliminaCorso(@PathVariable Long id) {
//        corsoService.deleteById(id);
//    }
}

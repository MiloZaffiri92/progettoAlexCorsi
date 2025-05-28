package org.example.academycorsi.controller;

import org.example.academycorsi.data.dto.CorsoDTO;
import org.example.academycorsi.service.CorsoService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping
    public CorsoDTO salvaCorso(@RequestBody CorsoDTO corsoDTO) {
        return corsoService.save(corsoDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CorsoDTO> modificaCorso(@PathVariable Long id,
                                                  @RequestBody CorsoDTO corso) {
        CorsoDTO corsoDTO = corsoService.updateCorso(id, corso);
        return ResponseEntity.ok(corsoDTO);
    }

    @DeleteMapping("/{id}")
    public void eliminaCorso(@PathVariable Long id) {
        corsoService.deleteById(id);
    }
}

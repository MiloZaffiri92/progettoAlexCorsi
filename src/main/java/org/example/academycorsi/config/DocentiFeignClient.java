package org.example.academycorsi.config;

import org.example.academycorsi.data.dto.DocenteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "docenti-service",
        url = "${docenti-service.url}"
)
public interface DocentiFeignClient {

    @GetMapping("/docenti/lista")
    List<DocenteDTO> getAllDocenti();

    @GetMapping("/docenti/{id}")
    ResponseEntity<DocenteDTO> getDocenteById(@PathVariable("id") Long id);

    @PostMapping("/docenti")
    DocenteDTO createDocente(@RequestBody DocenteDTO docenteDTO);

    @PutMapping("/docenti/{id}")
    ResponseEntity<DocenteDTO> updateDocente(@PathVariable("id") Long id, @RequestBody DocenteDTO docenteDTO);

    @DeleteMapping("/docenti/{id}/delete")
    void deleteDocente(@PathVariable("id") Long id);

    @GetMapping("/docenti/cerca")
    ResponseEntity<List<DocenteDTO>> cercaDocentePerNome(@RequestParam String nome);
}

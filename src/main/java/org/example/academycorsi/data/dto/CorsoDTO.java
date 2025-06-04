package org.example.academycorsi.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CorsoDTO {
    private Long id;
    private String nome;
    private String annoAccademico;
    private Long docenteId;
    private DocenteDTO docente;
    private Set<DiscenteDTO> discenti;
}

package org.example.academycorsi.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CorsoDTO {
    private String nome;
    private String annoAccademico;
    private Long docenteId;
    private DocenteDTO docente;
}

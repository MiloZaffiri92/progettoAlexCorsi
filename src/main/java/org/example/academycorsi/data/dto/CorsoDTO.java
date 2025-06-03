package org.example.academycorsi.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

//prova

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CorsoDTO {
    private String nome;
    private String annoAccademico;
    private DocenteDTO docente;
    private Long docenteId;

}

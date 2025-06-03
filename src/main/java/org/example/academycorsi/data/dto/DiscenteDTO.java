package org.example.academycorsi.data.dto;

import lombok.Data;

@Data
public class DiscenteDTO {

    private String nome;
    private String cognome;

    public DiscenteDTO(String nome, String cognome) {
        this.nome = nome;
        this.cognome = cognome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }
}

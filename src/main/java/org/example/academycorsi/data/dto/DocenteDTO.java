package org.example.academycorsi.data.dto;

public class DocenteDTO {


    private String nome;
    private String cognome;

    public DocenteDTO(long id, String cognome, String nome) {
        this.cognome = cognome;
        this.nome = nome;
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

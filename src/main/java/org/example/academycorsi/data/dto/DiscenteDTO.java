package org.example.academycorsi.data.dto;

public class DiscenteDTO {

    private String nome;
    private String cognome;
    private Integer matricola;
    private String cittaResidenza;

    public Integer getMatricola() {
        return matricola;
    }

    public void setMatricola(Integer matricola) {
        this.matricola = matricola;
    }

    public String getCittaResidenza() {
        return cittaResidenza;
    }

    public void setCittaResidenza(String cittaResidenza) {
        this.cittaResidenza = cittaResidenza;
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

    public DiscenteDTO(String nome, String cognome, Integer matricola, String cittaResidenza) {
        this.nome = nome;
        this.cognome = cognome;
        this.matricola = matricola;
        this.cittaResidenza = cittaResidenza;
    }
}

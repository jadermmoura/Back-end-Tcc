package br.edu.ifrs.restinga.requisicoes.modelo;

import javax.persistence.Entity;
import javax.persistence.Transient;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

@Entity
public class Servidor extends Usuario implements Serializable{
    @Transient
    // Define o campo
    @JsonProperty("tipo")
    private final String tipo = "servidor";
    private int siape; 
    private String cargo; 

    public int getSiape() {
            return siape;
    }
    public void setSiape(int siape) {
            this.siape = siape;
    }
    public String getCargo() {
            return cargo;
    }
    public void setCargo(String cargo) {
            this.cargo = cargo;
    }
}
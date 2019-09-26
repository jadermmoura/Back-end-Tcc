package br.edu.ifrs.restinga.requisicoes.modelo;

import javax.persistence.Entity;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Professor extends Usuario{

	@Transient
	 // Define o campo
	 @JsonProperty("tipo")
	private final String tipo = "professor";
	
	private int siape; 
	private boolean coordenador;

    public int getSiape() {
        return siape;
    }

    public void setSiape(int siape) {
        this.siape = siape;
    }

    public boolean isCoordenador() {
        return coordenador;
    }

    public void setCoordenador(boolean coordenador) {
        this.coordenador = coordenador;
    }
	
	
}

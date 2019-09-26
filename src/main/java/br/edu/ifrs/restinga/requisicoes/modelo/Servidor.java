package br.edu.ifrs.restinga.requisicoes.modelo;


import javax.persistence.Entity;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Servidor extends Usuario{
	private int siape; 
	private String cargo; 
	
	@Transient
	 // Define o campo
	 @JsonProperty("tipo")
	private final String tipo = "servidor";
	
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

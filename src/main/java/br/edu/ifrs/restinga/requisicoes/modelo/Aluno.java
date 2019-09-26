package br.edu.ifrs.restinga.requisicoes.modelo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.persistence.ManyToOne;

@Entity
public class Aluno extends Usuario{

    
	@Transient
	 // Define o campo
	 @JsonProperty("tipo")
	private final String tipo = "aluno";

   
	
	private int matricula; 
	private Date dataIngresso;
	
	public int getMatricula() {
		return matricula;
	}
	public void setMatricula(int matricula) {
		this.matricula = matricula;
	}
	public Date getDataIngresso() {
		return dataIngresso;
	}
	public void setDataIngresso(Date dataIngresso) {
		this.dataIngresso = dataIngresso;
	} 
	
}

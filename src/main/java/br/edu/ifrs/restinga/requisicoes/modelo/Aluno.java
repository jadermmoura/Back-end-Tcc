package br.edu.ifrs.restinga.requisicoes.modelo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Transient;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Aluno extends Usuario implements Serializable{

    @Transient
     // Define o campo
    @JsonProperty("tipo")
    private final String tipo = "aluno";
    private int matricula; 
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
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

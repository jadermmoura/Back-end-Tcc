
package br.edu.ifrs.restinga.requisicoes.modelo;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class RequisicaoAproveitamento extends Requisicao{
    
    @Transient
     // Define o campo
     @JsonProperty("tipo")
    private final String tipo = "requisicao";
    
    private  String disciplinasCursadasAnterior;
    private  String analiseProfessor;

    public String getDisciplinasCursadasAnterior() {
        return disciplinasCursadasAnterior;
    }

    public void setDisciplinasCursadasAnterior(String disciplinasCursadasAnterior) {
        this.disciplinasCursadasAnterior = disciplinasCursadasAnterior;
    }

    public String getAnaliseProfessor() {
        return analiseProfessor;
    }

    public void setAnaliseProfessor(String analiseProfessor) {
        this.analiseProfessor = analiseProfessor;
    }
    
   
    
    
    
    
}

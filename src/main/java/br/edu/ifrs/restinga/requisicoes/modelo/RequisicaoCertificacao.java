
package br.edu.ifrs.restinga.requisicoes.modelo;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Entity;
import javax.persistence.Transient;


@Entity
public class RequisicaoCertificacao extends Requisicao{
    
    @Transient
     // Define o campo
     @JsonProperty("tipo")
    private final String tipo = "certificacao";
    
    private String formacaoAtividadeAnterior;
    private String criterioAvaliacao;

    public String getFormacaoAtividadeAnterior() {
        return formacaoAtividadeAnterior;
    }

    public void setFormacaoAtividadeAnterior(String formacaoAtividadeAnterior) {
        this.formacaoAtividadeAnterior = formacaoAtividadeAnterior;
    }

    public String getCriterioAvaliacao() {
        return criterioAvaliacao;
    }

    public void setCriterioAvaliacao(String criterioAvaliacao) {
        this.criterioAvaliacao = criterioAvaliacao;
    }
    
    
}

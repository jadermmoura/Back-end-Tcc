
package br.edu.ifrs.restinga.requisicoes.modelo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
//Configurando herança
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "tipo")
//define o tipo raiz
@JsonTypeName("requisicao")
//tem que definir as subclasses conhecidas
@JsonSubTypes({
    @JsonSubTypes.Type(name = "aproveitamento", value = RequisicaoAproveitamento.class),
    @JsonSubTypes.Type(name = "certificacao", value = RequisicaoCertificacao.class)})
public abstract class Requisicao implements Serializable {
    
   
    private static final long serialVersionUID = 1L;
    @Transient
    @JsonProperty("tipo")
    private String tipo ="requisicao";
    @Id	
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date dataRequisicao;
    private String parecer;
    private boolean deferido;
    @Column(columnDefinition="LONGTEXT")
    private String anexos;
    @ManyToOne
    private Disciplina disciplinaSolicitada;
    @ManyToOne
    private Usuario usuario;
  
    
    public String getAnexos() {
		return anexos;
	}


	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Date getDataRequisicao() {
        return dataRequisicao;
    }

    public void setDataRequisicao(Date dataRequisicao) {
        this.dataRequisicao = dataRequisicao;
    }
 

    public String getParecer() {
        return parecer;	
    }

    public void setParecer(String parecer) {
        this.parecer = parecer;
    }

    public boolean isDeferido() {
        return deferido;
    }

    public void setDeferido(boolean deferido) {
        this.deferido = deferido;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setAnexos(String anexos) {
        this.anexos = anexos;
    }

    public Disciplina getDisciplinaSolicitada() {
        return disciplinaSolicitada;
    }

    public void setDisciplinaSolicitada(Disciplina disciplinaSolicitada) {
        this.disciplinaSolicitada = disciplinaSolicitada;
    }
  
}


package br.edu.ifrs.restinga.requisicoes.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

@Entity
//Configurando herança
@Inheritance(strategy = InheritanceType.JOINED)

//define o tipo raiz
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME, 
    include = JsonTypeInfo.As.EXISTING_PROPERTY, 
    property = "tipo"
)
@JsonTypeName("requisicao")

//tem que definir as subclasses conhecidas
@JsonSubTypes({
    @JsonSubTypes.Type(
        name = "aproveitamento", 
        value = RequisicaoAproveitamento.class
    ),
    @JsonSubTypes.Type(
        name = "certificacao", 
        value = RequisicaoCertificacao.class
    )
})

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
    private String deferido ;
            
    @ManyToOne
    private Disciplina disciplinaSolicitada;
    
    @ManyToOne
    private Usuario usuario;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(nullable = false)
    private List<Anexo> anexos;    

    public Requisicao(String parecer, String deferido) {
        this.parecer = parecer;
        this.deferido = deferido;
    }

    public Requisicao() {
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

    public void setDeferido(String deferido) {
        this.deferido = deferido;
    }

    public void setParecer(String parecer) {
        this.parecer = parecer;
    }

    public String getDeferido() {
        return deferido;
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

    public Disciplina getDisciplinaSolicitada() {
        return disciplinaSolicitada;
    }

    public void setDisciplinaSolicitada(Disciplina disciplinaSolicitada) {
        this.disciplinaSolicitada = disciplinaSolicitada;
    }

    public List<Anexo> getAnexos() {
        return anexos;
    }

    public void setAnexos(List<Anexo> anexos) {
        this.anexos = anexos;
    }
}

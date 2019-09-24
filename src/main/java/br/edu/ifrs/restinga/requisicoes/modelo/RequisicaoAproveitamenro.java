/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.requisicoes.modelo;

import javax.persistence.Entity;

/**
 *
 * @author jader
 */
@Entity
public class RequisicaoAproveitamenro extends Requisicao{
    
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

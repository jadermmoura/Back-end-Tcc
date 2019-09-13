/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.requisicoes.dao;

import br.edu.ifrs.restinga.requisicoes.modelo.Requisicao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequisicaoDAO extends CrudRepository<Requisicao, Integer> {
    
}
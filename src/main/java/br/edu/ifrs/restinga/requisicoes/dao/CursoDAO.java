/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.requisicoes.dao;

import br.edu.ifrs.restinga.requisicoes.modelo.Curso;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoDAO extends CrudRepository<Curso, Integer> {

    public Optional<Curso> findAllById(long id);
    
}
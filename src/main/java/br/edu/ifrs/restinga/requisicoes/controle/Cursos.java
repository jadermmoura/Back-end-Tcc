/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.requisicoes.controle;

import br.edu.ifrs.restinga.requisicoes.dao.CursoDAO;
import br.edu.ifrs.restinga.requisicoes.erros.NaoEncontrado;
import br.edu.ifrs.restinga.requisicoes.modelo.Curso;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Jim
 */
@RestController
@RequestMapping(path = "/api/cursos")
public class Cursos {

    @Autowired
    CursoDAO cursoDAO;

    @RequestMapping(path = "/")
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Curso> listar() {
        return cursoDAO.findAll();
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Curso pesquisarPeloId(@PathVariable long id) {
        Optional<Curso> cursoId = cursoDAO.findAllById(id);
        if (cursoId.isPresent()) {
            return cursoId.get();
        } else {
            throw new NaoEncontrado("Id não encontrado");
        }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.requisicoes.controle;

import br.edu.ifrs.restinga.requisicoes.dao.AlunoDAO;
import br.edu.ifrs.restinga.requisicoes.erros.NaoEncontrado;
import br.edu.ifrs.restinga.requisicoes.modelo.Aluno;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/alunos")
public class Alunos {

    @Autowired
    AlunoDAO alunoDAO;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Aluno> listar() {
        return alunoDAO.findAll();

    }

    @RequestMapping(path = "/", method = RequestMethod.POST)
    public Aluno inserir(@RequestBody Aluno aluno) {
        return alunoDAO.save(aluno);

    }

    @RequestMapping(path = "/{matricula}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Aluno pesquisarPeloMatricula(@PathVariable int matricula) {
        Optional<Aluno> matriculaAluno = alunoDAO.findAllByMatricula(matricula);
        if (matriculaAluno.isPresent()) {
            return matriculaAluno.get();
        } else {
            throw new NaoEncontrado("Matricula não encontrado");
        }
    }

    @RequestMapping(path = "/nome/{nome}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Aluno> buscarPeloNome(@PathVariable("nome") String nome) {
        return alunoDAO.findByNome(nome);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void apagar(@PathVariable int id) {
        if (alunoDAO.existsById(id)) {
            alunoDAO.deleteById(id);
        } else {
            throw new NaoEncontrado("Não encontrado");
        }
    }

    @RequestMapping(path = "/editar/{matricula}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public Aluno editar(@PathVariable int matricula, @RequestBody Aluno alunoNovo) {
        alunoNovo.setMatricula(0);
        Aluno alunoAntigo = this.pesquisarPeloMatricula(matricula);
        alunoAntigo.setEmail(alunoNovo.getEmail());
        alunoAntigo.setDataIngresso(alunoNovo.getDataIngresso());
        alunoAntigo.setNome(alunoNovo.getNome());
        return alunoDAO.save(alunoAntigo);

    }

}

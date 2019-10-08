/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.requisicoes.controle;

import br.edu.ifrs.restinga.requisicoes.dao.RequisicaoDAO;
import br.edu.ifrs.restinga.requisicoes.erros.ErroServidor;
import br.edu.ifrs.restinga.requisicoes.modelo.Requisicao;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping(path = "/api/requisicoes")
public class RequisicoesControle {

    private static Date horaSistema() {
        Date date = new Date();
        return date;
    }

    @Autowired
    RequisicaoDAO rDao;

    @GetMapping(path = "/")
    public ResponseEntity<?> listarRequisicao() {
        Iterable<Requisicao> r = rDao.findAll();
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    @PostMapping(path = "/")
    public ResponseEntity<Requisicao> insere(@RequestBody Requisicao c) {
        c.setDataRequisicao(horaSistema());
        Requisicao novaRequisicao = rDao.save(c);
        if (novaRequisicao != null) {
            return new ResponseEntity<>(novaRequisicao, HttpStatus.CREATED);
        }
        throw new ErroServidor("nao foi possivel salvar a requisição");
    }

    public void atualizaRequisicao() {

    }
}

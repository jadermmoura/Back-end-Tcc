/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.requisicoes.controle;

import br.edu.ifrs.restinga.requisicoes.dao.AlunoDAO;
import br.edu.ifrs.restinga.requisicoes.dao.DisciplinaDAO;
import br.edu.ifrs.restinga.requisicoes.dao.ProfessorDAO;
import br.edu.ifrs.restinga.requisicoes.dao.RequisicaoDAO;
import br.edu.ifrs.restinga.requisicoes.erros.ErroServidor;
import br.edu.ifrs.restinga.requisicoes.erros.NaoEncontrado;
import br.edu.ifrs.restinga.requisicoes.erros.RequisicaoInvalida;
import br.edu.ifrs.restinga.requisicoes.modelo.Aluno;
import br.edu.ifrs.restinga.requisicoes.modelo.Professor;
import br.edu.ifrs.restinga.requisicoes.modelo.Requisicao;
import br.edu.ifrs.restinga.requisicoes.modelo.RequisicaoAproveitamento;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping(path = "/api/requisicoes")
public class RequisicoesControle {

	private static Date horaSistema() {
		Date date = new Date();
		return date;
	}
        private void validaRequisicao(Requisicao c){
            RequisicaoAproveitamento cer = new RequisicaoAproveitamento();
//            if (c.getDisciplinaSolicitada() == null) {
//               throw new RequisicaoInvalida("disciplina e obrigatorio");
//            }
            if(c instanceof RequisicaoAproveitamento){
               if (cer.getDisciplinasCursadasAnterior() == null || cer.getDisciplinasCursadasAnterior().isEmpty()) {
               throw  new RequisicaoInvalida("disciplina cursada anteriormente não pode ser vazio");                }
 
            }
        }

	@Autowired
	RequisicaoDAO rDao;

	@Autowired
	DisciplinaDAO dDao;

	@Autowired
	AlunoDAO aDao;

	@Autowired
	ProfessorDAO pDao;

	@GetMapping(path = "/")
	public ResponseEntity<?> listarRequisicao() {
		Iterable<Requisicao> r = rDao.findAll();
		return new ResponseEntity<>(r, HttpStatus.OK);
	}

	@PostMapping(path = "/")
	public ResponseEntity<Requisicao> insere(@RequestBody Requisicao c) {
		c.setDataRequisicao(horaSistema());
                validaRequisicao(c);
                Requisicao novaRequisicao = rDao.save(c);
		if (novaRequisicao != null) {
			return new ResponseEntity<>(novaRequisicao, HttpStatus.CREATED);
		}
		throw new ErroServidor("Não foi possivel salvar a requisição");
	}
	/*
	 * 1. Por disciplina 2. Por periodos 3. Por aluno 4. Por professor responsável
	 */

	@GetMapping("/busca-requisicao-pela-disciplina/{id}")
	public ResponseEntity<List<Requisicao>> requisicaoPorDisciplina(@PathVariable Long id) {
		List<Requisicao> requisicao = rDao.findByDisciplinaSolicitada(dDao.findById(id).get());
		if (requisicao.isEmpty()) {
			throw new NaoEncontrado("Não foi possível achar registro contendo a disciplina especificada.");
		}
		return new ResponseEntity<List<Requisicao>>(requisicao, HttpStatus.OK);
	}

	@GetMapping("/busca-requisicao-por-periodos/{inicio}/{fim}")
	public ResponseEntity<List<Requisicao>> requisicaoEntrePeriodos(@PathVariable Date inicio, @PathVariable Date fim) {
		List<Requisicao> requisicoes = (List<Requisicao>) this.listarRequisicao();

		List<Requisicao> aux = new ArrayList<>();
		requisicoes.forEach(x -> {
			if (x.getDataRequisicao().after(inicio) && x.getDataRequisicao().before(fim)) {
				aux.add(x);
			}
		});
		if (aux.isEmpty()) {
			throw new NaoEncontrado("Não foi possível encontrar um registro no período solicitado. ");
		}

		return new ResponseEntity<List<Requisicao>>(aux, HttpStatus.OK);
	}

//	@GetMapping("/busca-requisicoes-por-aluno/{idAluno}")
//	public ResponseEntity<List<Requisicao>> requisicaosPorAluno(@PathVariable Long idAluno) {
//		Optional<Aluno> aluno = aDao.findById(idAluno);
//		if (aluno.isPresent()) {
//			if (!aluno.get().getRequisicoes().isEmpty()) {
//				return new ResponseEntity<List<Requisicao>>(aluno.get().getRequisicoes(), HttpStatus.OK);
//			}
//			throw new NaoEncontrado("Não foi possível encontrar as requisições desse aluno. ");
//		} else {
//
//			throw new NaoEncontrado("O aluno especificado, não foi encontrado no sistema.");
//		}
//
//	}

//	@GetMapping("/busca-requisicoes-por-professor/{idProfessor}")
//	public ResponseEntity<List<Requisicao>> requisicaosPorProfessor(@PathVariable Long idProfessor) {
//
//		Optional<Professor> teste = pDao.findById(idProfessor);
//		if (teste.isPresent()) {
//			if (!teste.get().getRequisicoes().isEmpty()) {
//				return new ResponseEntity<List<Requisicao>>(teste.get().getRequisicoes(), HttpStatus.OK);
//			}
//			throw new NaoEncontrado("Não foi possível encontrar as requisições atrelados a esse professor. ");
//		} else {
//			throw new NaoEncontrado("O professor especificado, não foi encontrado no sistema.");
//			
//		}
//
//	}

}

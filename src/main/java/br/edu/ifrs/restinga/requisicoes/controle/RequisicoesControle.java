package br.edu.ifrs.restinga.requisicoes.controle;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import br.edu.ifrs.restinga.requisicoes.dao.AlunoDAO;
import br.edu.ifrs.restinga.requisicoes.dao.AnexoDAO;
import br.edu.ifrs.restinga.requisicoes.dao.DisciplinaDAO;
import br.edu.ifrs.restinga.requisicoes.dao.ProfessorDAO;
import br.edu.ifrs.restinga.requisicoes.dao.RequisicaoAproveitamentoDAO;
import br.edu.ifrs.restinga.requisicoes.dao.RequisicaoCertificacaoDAO;
import br.edu.ifrs.restinga.requisicoes.dao.RequisicaoDAO;
import br.edu.ifrs.restinga.requisicoes.erros.ErroServidor;
import br.edu.ifrs.restinga.requisicoes.erros.NaoEncontrado;
import br.edu.ifrs.restinga.requisicoes.erros.RequisicaoInvalida;
import br.edu.ifrs.restinga.requisicoes.modelo.Requisicao;
import br.edu.ifrs.restinga.requisicoes.modelo.RequisicaoAproveitamento;
import br.edu.ifrs.restinga.requisicoes.modelo.RequisicaoCertificacao;

@RestController
@CrossOrigin
@RequestMapping(path = "/api/requisicoes")
public class RequisicoesControle {
    
    @Autowired
    RequisicaoDAO rDao;
    
    @Autowired
    RequisicaoAproveitamentoDAO rpro;
    
    @Autowired
    RequisicaoCertificacaoDAO rcert;

    @Autowired
    DisciplinaDAO dDao;

    @Autowired
    AlunoDAO aDao;

    @Autowired
    ProfessorDAO pDao;

    @Autowired
    AnexoDAO anexoDao;
    
    private static Date horaSistema() {
        return new Date();
    }

    private void validaRequisicao(Requisicao c) {
        if (c.getDisciplinaSolicitada() == null) {
            throw new RequisicaoInvalida("disciplina e obrigatorio");
        }
        if (c.getAnexos() == null || c.getAnexos().isEmpty()) {
            throw new RequisicaoInvalida("anexos e obrigatorio");
        }
        if (c instanceof RequisicaoAproveitamento) {
            if (((RequisicaoAproveitamento) c).getDisciplinasCursadasAnterior() == null
                    || ((RequisicaoAproveitamento) c).getDisciplinasCursadasAnterior().isEmpty()) {
                throw new RequisicaoInvalida("disciplina cursada anteriormente não pode ser vazio");
            }
        }
        if (c instanceof RequisicaoCertificacao) {
            if (((RequisicaoCertificacao) c).getFormacaoAtividadeAnterior() == null
                    || ((RequisicaoCertificacao) c).getFormacaoAtividadeAnterior().isEmpty()) {
                throw new RequisicaoInvalida("formação ou atividade anterior e obrigatorio");
            }
        }
        for (int i = 0; i < c.getAnexos().size(); i++) {
            if (c.getAnexos().get(i).getArquivo() == null || c.getAnexos().get(i).getArquivo().isEmpty()) {
                throw new RequisicaoInvalida("arquivo e obrigatorio em anexos");
            }
            if (c.getAnexos().get(i).getNome() == null || c.getAnexos().get(i).getNome().isEmpty()) {
                throw new RequisicaoInvalida("nome e obrigatorio em anexos");
            }
            if (c.getAnexos().get(i).getTamanho() == null || c.getAnexos().get(i).getTamanho().isEmpty()) {
                throw new RequisicaoInvalida("tamanho e obrigatorio em anexos");
            }
            if (c.getAnexos().get(i).getTipo() == null || c.getAnexos().get(i).getTipo().isEmpty()) {
                throw new RequisicaoInvalida("tipo e obrigatorio em anexos");
            }
        }
    }
    
    @GetMapping(path = "/aproveitamento/")
    public ResponseEntity<?> listaAproveitamento(){
        List <RequisicaoAproveitamento> apro = rpro.findAll();
        return new ResponseEntity<>(apro,HttpStatus.OK);
    }
    
    @GetMapping(path = "/certificacao/")
    public ResponseEntity<?> listaCertificacao(){
        List<RequisicaoCertificacao> cert = rcert.findAll();
        return  new ResponseEntity<>(cert,HttpStatus.OK);
    }

    @GetMapping(path = "/")
    public ResponseEntity<?> listarRequisicao() {
        Iterable<Requisicao> r = rDao.findAll();
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    @PostMapping(path = "/")
    public ResponseEntity<Requisicao> insere(@RequestBody Requisicao requisicao) {
        requisicao.setDataRequisicao(horaSistema());
        validaRequisicao(requisicao);
        
        Requisicao novaRequisicao = rDao.save(requisicao);
        
        if (novaRequisicao != null) {
            return new ResponseEntity<>(novaRequisicao, HttpStatus.CREATED);
        }
        throw new ErroServidor("Não foi possivel salvar a requisição");
    }
   
    @GetMapping(path = "/requisicaoPorPeriodo/")
    public Iterable <Requisicao> pesquisaPorPeriodo(
            @RequestParam (required = false) Date inicio,
            @RequestParam(required = false) Date fim){
        if (inicio != null || fim != null) {
            return rDao.findByDataRequisicaoBetween(inicio, fim);
        }else{
            throw new RequisicaoInvalida("digite uma data valida");
        }        
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

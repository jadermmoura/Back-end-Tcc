package br.edu.ifrs.restinga.requisicoes.controle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import br.edu.ifrs.restinga.requisicoes.autenticacao.MeuUser;
import br.edu.ifrs.restinga.requisicoes.dao.*;
import br.edu.ifrs.restinga.requisicoes.modelo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import br.edu.ifrs.restinga.requisicoes.erros.ErroServidor;
import br.edu.ifrs.restinga.requisicoes.erros.NaoEncontrado;
import br.edu.ifrs.restinga.requisicoes.erros.RequisicaoInvalida;
import java.util.Iterator;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

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
    UsuarioDAO uDao;

    @Autowired
    ProfessorDAO pDao;

    @Autowired
    AnexoDAO anexoDao;

    private static Date horaSistema() {
        return new Date();
    }

    private void validaRequisicao(Requisicao c) {
        if (c.getDisciplinaSolicitada() == null) {
            throw new RequisicaoInvalida("Disciplina é obrigatória. ");
        }
        if (c.getAnexos() == null || c.getAnexos().isEmpty()) {
            throw new RequisicaoInvalida("Anexo é obrigatório. ");
        }
        if (c instanceof RequisicaoAproveitamento) {
            if (((RequisicaoAproveitamento) c).getDisciplinasCursadasAnterior() == null
                    || ((RequisicaoAproveitamento) c).getDisciplinasCursadasAnterior().isEmpty()) {
                throw new RequisicaoInvalida("Não forneceram a disciplina cursada anteriormente.");
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
                throw new RequisicaoInvalida("Arquivo é obrigatório. ");
            }
            if (c.getAnexos().get(i).getNome() == null || c.getAnexos().get(i).getNome().isEmpty()) {
                throw new RequisicaoInvalida("Nome do anexo é obrigatório.");
            }
            if (c.getAnexos().get(i).getTamanho() == null || c.getAnexos().get(i).getTamanho().isEmpty()) {
                throw new RequisicaoInvalida("Não contém o tamanho do anexo. ");
            }
            if (c.getAnexos().get(i).getTipo() == null || c.getAnexos().get(i).getTipo().isEmpty()) {
                throw new RequisicaoInvalida("Tipo de anexo é obrigatório.");
            }
        }
    }

    @GetMapping(path = "/aproveitamento/")
    public ResponseEntity<?> listaAproveitamento() {
        List<RequisicaoAproveitamento> apro = rpro.findAll();
        return new ResponseEntity<>(apro, HttpStatus.OK);
    }

    @GetMapping(path = "/certificacao/")
    public ResponseEntity<?> listaCertificacao() {
        List<RequisicaoCertificacao> cert = rcert.findAll();
        return new ResponseEntity<>(cert, HttpStatus.OK);
    }

    @GetMapping(path = "/")
    public ResponseEntity<?> listarRequisicao() {
        Iterable<Requisicao> r = rDao.findAll();
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> listarRequisicao(@PathVariable Long id) {
        Requisicao requi = rDao.findById(id).get();
        return new ResponseEntity<>(requi, HttpStatus.OK);
    }

    @PostMapping(path = "/")
    public ResponseEntity<Requisicao> insere(@RequestBody Requisicao requisicao) {
        requisicao.setDataRequisicao(horaSistema());
        requisicao.setDeferido("Em análise");
        validaRequisicao(requisicao);
        Requisicao novaRequisicao = rDao.save(requisicao);
        System.out.println(novaRequisicao);
        if (novaRequisicao != null) {
            return new ResponseEntity<>(novaRequisicao, HttpStatus.CREATED);
        }
        throw new ErroServidor("Não foi possivel salvar a requisição");
    }


    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Requisicao> editarRequisicao(@RequestBody Requisicao novaRequisicao, @PathVariable long id) {
        Requisicao requi = (Requisicao) this.listarRequisicao(id).getBody();
        requi.setDeferido(novaRequisicao.getDeferido());
        requi.setParecer(novaRequisicao.getParecer());
         return new ResponseEntity<>(rDao.save(requi), HttpStatus.OK);
    }
    @GetMapping(path = "/requisicaoPorPeriodo/")
    public Iterable<Requisicao> pesquisaPorPeriodo(
            @RequestParam(required = false) Date inicio,
            @RequestParam(required = false) Date fim) {
        if (inicio != null || fim != null) {
            return rDao.findByDataRequisicaoBetween(inicio, fim);
        } else {
            throw new RequisicaoInvalida("Digite uma data valida");
        }
    }
    @GetMapping("/busca-requisicao-pela-disciplina/{id}")
    public Iterable<Requisicao> requisicaoPorDisciplina(@AuthenticationPrincipal MeuUser usuarioAutenticado, @PathVariable Long id) {
        Iterable<Requisicao> listaRequisicao = new ArrayList<>();
        if (usuarioAutenticado.getUsuario().getPermissoes().contains("ensino")) {
            listaRequisicao = rDao.findByDisciplinaSolicitada(dDao.findById(id).get());
            if (listaRequisicao == null) {
                throw new NaoEncontrado("Não foi possível achar registro contendo a disciplina especificada.");
            } else {
                return listaRequisicao;
            }
        } else {
            throw new NaoEncontrado("O usuário não tem permissão de ensino. ");
        }
    }

    @GetMapping("/requisicao-periodos/{inicio}/{fim}")
    public List<Requisicao> requisicaoEntrePeriodos(@PathVariable Date inicio, @PathVariable Date fim, @AuthenticationPrincipal MeuUser usuarioAutenticado) {
        List<Requisicao> aux = new ArrayList<>();

        if (usuarioAutenticado.getUsuario().getPermissoes().contains("ensino")) {

            List<Requisicao> requisicoes = (List<Requisicao>) this.listarRequisicao();

            requisicoes.forEach(x -> {
                if (x.getDataRequisicao().after(inicio) && x.getDataRequisicao().before(fim)) {
                    aux.add(x);
                }
            });
            if (aux.isEmpty()) {
                throw new NaoEncontrado("Não foi possível encontrar um registro no período solicitado. ");
            }
        }
        return aux;
    }

    @GetMapping("/requisicoes-por-aluno/{idAluno}")
    public Iterable<Requisicao> requisicaosPorAluno(@PathVariable Long idAluno, @AuthenticationPrincipal MeuUser usuarioAutenticado) {
        Optional<Usuario> aluno = null;
        if (usuarioAutenticado.getUsuario().getId() == idAluno
                || usuarioAutenticado.getUsuario().getPermissoes().contains("ensino")) {
            Iterable<Requisicao> requisicoes = new ArrayList<>();

            aluno = uDao.findById(idAluno);
            if (aluno.isPresent()) {
                requisicoes = rDao.findByUsuario(aluno.get());
                if (requisicoes != null) {
                    return requisicoes;
                } else {
                    throw new NaoEncontrado("Não foi possível encontrar as requisicoes do aluno");
                }
            } else {

                throw new NaoEncontrado("O aluno especificado, não foi encontrado no sistema.");
            }
        } else {
            throw new NaoEncontrado("Você não é o aluno pesquisado ou você não tem permissão de ensino para realizar a consulta. ");
        }
    }

    @GetMapping("/requisicoes-por-professor/{idProfessor}")
    public Iterable<Requisicao> requisicoesPorProfessor(@AuthenticationPrincipal MeuUser usuarioAutenticado, @PathVariable Long idProfessor) {

        if (usuarioAutenticado.getUsuario().getPermissoes().contains("ensino")
                || usuarioAutenticado.getUsuario().getId() == idProfessor) {
            Optional<Usuario> professor = uDao.findById(idProfessor);
            if (professor.isPresent()) {
                Iterable<Requisicao> requisicoes = rDao.findByUsuario(professor.get());
                if (requisicoes != null) {
                    return requisicoes;
                } else {
                    throw new NaoEncontrado("Não foi possível encontrar requisição");
                }
            }
            throw new NaoEncontrado("Não foi possível encontrar as requisições atreladas a esse professor. ");
        } else {
            throw new NaoEncontrado("O ID fornecido ou o requerente não tem permissão para listar.");
        }
    }

    @RequestMapping(path = "/nome/{nome}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Requisicao> buscarNomeDisciplina(@PathVariable("nome") String nome) {
        Iterable<Requisicao> listaRequisicao = rDao.findAll();
        List nova = new ArrayList();
        for (Requisicao requisicao : listaRequisicao) {
            if (requisicao.getDisciplinaSolicitada().getNome().equals(nome)) {
                nova.add(requisicao);
            }
        }
        return nova;
    }

}

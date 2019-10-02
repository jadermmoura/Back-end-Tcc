/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.requisicoes.controle;

import br.edu.ifrs.restinga.requisicoes.dao.CursoDAO;
import br.edu.ifrs.restinga.requisicoes.dao.DisciplinaDAO;
import br.edu.ifrs.restinga.requisicoes.erros.ErroServidor;
import br.edu.ifrs.restinga.requisicoes.erros.NaoEncontrado;
import br.edu.ifrs.restinga.requisicoes.erros.RequisicaoInvalida;
import br.edu.ifrs.restinga.requisicoes.modelo.Curso;
import br.edu.ifrs.restinga.requisicoes.modelo.Disciplina;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@RequestMapping(path = "/api/cursos")
public class CursosControle {

    @Autowired
    CursoDAO cursoDAO;

    @Autowired
    DisciplinaDAO disciplinaDAO;
    
    
    ///////////////////// LISTAR curso //////////////////////////    
    
    @RequestMapping(path = "/", method = RequestMethod.GET)
    public Iterable<Curso> listar() {
        return cursoDAO.findAll();
    }

///////////////////// INSERIR curso //////////////////////////    

    @RequestMapping(path = "/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Curso inserir(@RequestBody Curso curso) {
        curso.setId(Long.MIN_VALUE);
        
        return cursoDAO.save(curso);
        
    }
    
///////////////////// BUSCAR curso PELA ID //////////////////////////        

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Curso recuperar(@PathVariable Long id) {
        Optional<Curso> findById = cursoDAO.findById(id);
        if (findById.isPresent()) {
            return findById.get();
        } else {
            throw new NaoEncontrado("Não encontrado");
        }
    }
    
///////////////////// BUSCAR USUÁRIO PELO NOME //////////////////////////   
//    
//    @RequestMapping( path = "/usuarios/pesquisar/nome/", method = RequestMethod.GET)
//    public Iterable<Usuario> pesquisaPorNome(
//            @RequestParam(required = false) String inicia, 
//            @RequestParam(required = false) String contem ) {
//        if(inicia!=null) {
//            return usuarioDAO.findByNomeStartingWith(inicia);
//        }
//        if(contem!=null) {
//            return usuarioDAO.findByNomeContaining(contem);
//        }
//            throw new RequisicaoInvalida("Digite algo para buscar...");
//    }    
    
//    
/////////////////////// BUSCAR USUÁRIO PELO CPF //////////////////////////   
//    
//    @RequestMapping( path = "/usuarios/pesquisar/cpf/", method = RequestMethod.GET)
//    public Iterable<Usuario> pesquisaPorCpf(
//            @RequestParam(required = false) String contem ) {
//        if(contem!=null) {
//            return usuarioDAO.findByCpfContaining(contem);
//        }
//            throw new RequisicaoInvalida("Digite um CPF para buscar...");
//    }    
//    
//    
/////////////////////// BUSCAR USUÁRIO PELO EMAIL //////////////////////////   
//    
//    @RequestMapping( path = "/usuarios/pesquisar/email/", method = RequestMethod.GET)
//    public Iterable<Usuario> pesquisaPorEmail(
//            @RequestParam(required = false) String contem ) {
//        if(contem!=null) {
//            return usuarioDAO.findByEmailContaining(contem);
//        }
//            throw new RequisicaoInvalida("Digite um email para buscar...");
//    }    
    
///////////////////// ATUALIZAR USUÁRIO PELA ID //////////////////////////        

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void atualizar(@PathVariable Long id, @RequestBody Curso curso){
        if (cursoDAO.existsById(id)){
            curso.setId(id);
            //validaUsuario(curso);
            cursoDAO.save(curso);
        }else{
            throw new NaoEncontrado("Não encontrado");
        }
    }
    
///////////////////// APAGAR USUÁRIO PELA ID //////////////////////////        

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void apagar(@PathVariable Long id){
        if (cursoDAO.existsById(id)){
           cursoDAO.deleteById(id);
        }else {
            throw new NaoEncontrado("Não encontrado");
        }
    }
    
///////////////////// LISTAR TELEFONES DO USUÁRIO PELA ID //////////////////////////        
   
    @RequestMapping(path = "/{idCurso}/disciplinas",method = RequestMethod.GET)
    public Iterable<Disciplina> listarDisciplinas(@PathVariable Long idCurso) {
        return this.recuperar(idCurso).getDisciplinas();
    }
    
///////////////////// INSERIR TELEFONE NO USUÁRIO PELA ID //////////////////////////        
    
    @RequestMapping(path = "/{idCurso}/disciplinas",method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Disciplina inserirDisciplina(@PathVariable Long idCurso,@RequestBody Disciplina disciplina) {
        //disciplina.setId(Long.MIN_VALUE);
        Disciplina disciplinaSalvo = disciplinaDAO.save(disciplina);
        Curso curso = this.recuperar(idCurso);
        curso.getDisciplinas().add(disciplinaSalvo);
        cursoDAO.save(curso);
        return disciplinaSalvo;
    }
    
///////////////////// LISTAR TELEFONE DO USUÁRIO PELA ID DO TELEFONE E DO PRÓPRIO USUÁRIO //////////////////////////        
    
    @RequestMapping(path = "/{idCurso}/disciplinas/{id}", method = RequestMethod.GET)
    public Disciplina recuperarDisciplina(@PathVariable Long idCurso, @PathVariable Long id) {
        Optional<Disciplina> findById = disciplinaDAO.findById(id);
        if(findById.isPresent())
            return findById.get();
        else 
            throw new NaoEncontrado("Não encontrado");
    }
    
///////////////////// ATUALIZAR TELEFONE DO USUÁRIO PELA ID DO TELEFONE E DO PRÓPRIO USUÁRIO //////////////////////////        
    
    @RequestMapping(path = "/{idCurso}/disciplinas/{id}",method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void atualizarDisciplina(@PathVariable Long idCurso, @PathVariable Long id,
            @RequestBody Disciplina disciplina){
        if(disciplinaDAO.existsById(id)){
            disciplina.setId(id);
            disciplinaDAO.save(disciplina);
        } else 
            throw new NaoEncontrado("Não encontrado");
    }
    
///////////////////// APAGAR TELEFONE DO USUÁRIO PELA ID DO TELEFONE E DO PRÓPRIO USUÁRIO //////////////////////////        
    
    @RequestMapping(path= "/{idCurso}/disciplinas/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void apagarDisciplina(@PathVariable Long idCurso,@PathVariable Long id) {
        Disciplina disciplinaAchada=null;
        Curso curso = this.recuperar(idCurso);
        List<Disciplina> disciplinas = curso.getDisciplinas();
        for (Disciplina disciplinaLista : disciplinas) {
            if(id==disciplinaLista.getId())
                disciplinaAchada= disciplinaLista;
        }
        if(disciplinaAchada!=null) {
            curso.getDisciplinas().remove(disciplinaAchada);
            cursoDAO.save(curso);
        } else 
            throw new NaoEncontrado("Não encontrado");
    }
    
    
    // CURSOS  
//    @GetMapping(path = "")
//    public ResponseEntity<?> listar() {
//    		Iterable<Curso> cursos = cursoDAO.findAll(); 
//    	if(cursos == null) {
//    		throw new NaoEncontrado("Não foi possível encontrar a lista de curso. "); 
//    	}
//        return new ResponseEntity<Iterable<Curso>>(cursoDAO.findAll(), HttpStatus.OK);
//    }
//    
//    @PostMapping(path="")
//    public ResponseEntity<Curso> inserirCurso(@RequestBody Curso curso){
//    	if(curso.getNome().isEmpty()) {
//    		throw new RequisicaoInvalida("Você não pode inserir um curso sem número. "); 
//    	}
//    	
//    	Curso novoCurso = cursoDAO.save(curso); 
//    	if(novoCurso != null) {
//    		return new ResponseEntity<>(novoCurso, HttpStatus.CREATED); 
//    	}
//    	throw new ErroServidor("Não foi possível salvar o curso especificado. "); 
//    }
//
//    @GetMapping(path = "/{id}")
//    public ResponseEntity<Curso> carregarCurso(@PathVariable long id) {
//        
//    	Optional<Curso> cursoId = cursoDAO.findAllById(id);
//        if (cursoId.isPresent()) {
//            return new ResponseEntity<>(cursoId.get(), HttpStatus.OK);
//        } else {
//            throw new NaoEncontrado("Curso não encontrado");
//        }
//    }
//    
//    public ResponseEntity<Curso> editarCurso(@RequestBody Curso novoCurso, @PathVariable long id){
//    	Curso curso = this.carregarCurso(id).getBody(); 
//    	if(novoCurso.getNome() != null) {
//    		curso.setNome(novoCurso.getNome());
//    	}
//    	return new ResponseEntity<>(curso, HttpStatus.NO_CONTENT); 
//    }
//    
//    // DISCIPLINAS
//    
//    @GetMapping(path="/{id}/disciplinas")
//    public ResponseEntity<List<Disciplina>> listarDisciplinaPorCurso(@PathVariable long id){
//    	Curso curso = this.carregarCurso(id).getBody(); 
//    	if(curso.getDisciplinas() == null) {
//    		throw new NaoEncontrado("Não foi possível listar. "); 
//    	}
//    	return new ResponseEntity<>(curso.getDisciplinas(), HttpStatus.OK); 
//    }
//    
//    @PostMapping(path="/{id}/disciplinas")
//    public ResponseEntity<Disciplina> novaDisciplina(@RequestBody Disciplina disciplina, @PathVariable long id){
//    	Curso curso = this.carregarCurso(id).getBody();
//    	if(disciplina.getCargaHoraria() <= 0) {
//    		throw new RequisicaoInvalida("Você não pode inserir uma disciplina com carga horária igual ou menor que zero. "); 
//    	}
//    	if(disciplina.getNome().isEmpty()) {
//    		throw new RequisicaoInvalida("Você não pode inserir uma disciplina sem nome. "); 
//    	}
//    	curso.getDisciplinas().add(disciplina);
//    	Curso novoCurso = cursoDAO.save(curso); 
//    	return new ResponseEntity<>(novoCurso.getDisciplinas().get(novoCurso.getDisciplinas().size()-1), HttpStatus.CREATED); 
//    }
//    
//    @GetMapping(path="/{id}/disciplinas/{idDisciplina}")
//    public ResponseEntity<Disciplina> carregarDisciplina(@PathVariable long id, @PathVariable long idDisciplina){
//    	Optional<Disciplina> d= disciplinaDAO.findById(idDisciplina);
//    	if(d.isPresent()) {
//    		return new ResponseEntity<>(d.get(), HttpStatus.OK); 
//    	}
//    	throw new RequisicaoInvalida("Não foi possível encontrar a disciplina requisitada."); 
//    }
//    
//    @PatchMapping(path="/{id}/disciplinas/{idDisciplina}")
//    public ResponseEntity<Disciplina> atualizarDisciplina(@PathVariable long id, @PathVariable long idDisciplina, @RequestBody Disciplina novaDisciplina){
//    	Curso curso = this.carregarCurso(id).getBody(); 
//    	Disciplina d = this.carregarDisciplina(id, idDisciplina).getBody();
//    	
//    	if(novaDisciplina.getCargaHoraria() > 0) {
//    		d.setCargaHoraria(0);
//    	}
//    	if(!novaDisciplina.getNome().isEmpty()) {
//    		d.setNome(novaDisciplina.getNome());
//    	}
//    	disciplinaDAO.save(d); 
//    	return null; 
//    }
    
    
}

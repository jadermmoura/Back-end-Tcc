package br.edu.ifrs.restinga.requisicoes.controle;

import br.edu.ifrs.restinga.requisicoes.dao.RequisicaoDAO;
import br.edu.ifrs.restinga.requisicoes.dao.UsuarioDAO;
import br.edu.ifrs.restinga.requisicoes.erros.NaoEncontrado;
import br.edu.ifrs.restinga.requisicoes.erros.RequisicaoInvalida;
import br.edu.ifrs.restinga.requisicoes.modelo.Usuario;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;



@RestController
@CrossOrigin
@RequestMapping(path = "/api")
public class UsuariosControle {
    
   @Autowired
    UsuarioDAO usuarioDAO;
    
    @Autowired
    RequisicaoDAO requisicaoDAO;
    
///////////// LISTAR USUÁRIOS ////////////////////////       

    @RequestMapping(path = "/usuarios/", method = RequestMethod.GET)
    public Iterable<Usuario> listar() {
        return usuarioDAO.findAll();
    }

    @PostMapping("/usuarios")
    public Usuario novoUsuario(@RequestBody Usuario usuario){
        return usuarioDAO.save(usuario); 
    }
    
///////////// INSERIR USUÁRIO ////////////////////////           
    
//    @RequestMapping(path = "/usuarios/", method = RequestMethod.POST)
//    @ResponseStatus(HttpStatus.CREATED)
//    public Usuario inserir(@RequestBody Usuario usuario) {
//        usuario.setID(Long.MIN_VALUE);
//        //Validações aqui
//        //Vou verificar se o login existe:
//        Iterable<Usuario> usuarios = usuarioDAO.findAll();
//        boolean loginIgual = false;
//        for (Usuario loginEntrada : usuarios) {
//            if (loginEntrada.getLogin().equals(usuario.getLogin())) {
//                loginIgual = true;
//                break;
//            }
//        }
//        if (loginIgual== true) {
//            throw new RequisicaoInvalida("Este login JÁ EXISTE. Escolha outro");
//            
//        }
//        return usuarioDAO.save(usuario);
//    }
//    
///////////// RECUPERAR USUÁRIO PELA ID ////////////////////////               

    @RequestMapping(path = "/usuarios/{id}", method = RequestMethod.GET)
    public Usuario recuperar(@PathVariable long id) {
        Optional<Usuario> findById = usuarioDAO.findById(id);
        if (findById.isPresent()) {
            return findById.get();
        } else {
            throw new NaoEncontrado("USUÁRIO não encontrado");
        }
    }
    
///////////// ATUALIZAR USUÁRIO PELA ID ////////////////////////               

    @RequestMapping(path = "/usuarios/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void atualizar(@PathVariable long id, @RequestBody Usuario usuario){
        if (usuarioDAO.existsById(id)){
            usuario.setId(id);
            //Validações aqui
            usuarioDAO.save(usuario);
        }else{
            throw new NaoEncontrado("USUÁRIO não encontrado");
        }
    }
    
///////////// APAGAR USUÁRIO PELA ID ////////////////////////               
    
    @RequestMapping(path = "/usuarios/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void apagar(@PathVariable long id){
        if (usuarioDAO.existsById(id)){
            usuarioDAO.deleteById(id);
        }else {
            throw new NaoEncontrado("USUÁRIO não encontrado");
        }
    } 
}
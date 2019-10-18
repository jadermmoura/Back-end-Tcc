package br.edu.ifrs.restinga.requisicoes.controle;

import br.edu.ifrs.restinga.requisicoes.dao.RequisicaoDAO;
import br.edu.ifrs.restinga.requisicoes.dao.UsuarioDAO;
import br.edu.ifrs.restinga.requisicoes.erros.NaoEncontrado;
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
@RequestMapping(path = "/api/usuarios")
public class UsuariosControle {
    
   @Autowired
    UsuarioDAO usuarioDAO;
    
    @Autowired
    RequisicaoDAO requisicaoDAO;
    

///////////// LISTAR USUÁRIOS ////////////////////////       


    @RequestMapping(path = "/", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Usuario> listar() {
        return usuarioDAO.findAll();
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)

    public Usuario novoUsuario(@RequestBody Usuario usuario){
        return usuarioDAO.save(usuario); 
    }
    

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Usuario recuperar(@PathVariable long id) {
        Optional<Usuario> findById = usuarioDAO.findById(id);
        if (findById.isPresent()) {
            return findById.get();
        } else {
            throw new NaoEncontrado("USUÁRIO não encontrado");
        }
    }
//faltou atualizar siape , cargo , tipo    
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario atualizar(@PathVariable long id, @RequestBody Usuario usuario){
        if (usuarioDAO.existsById(id)){
            usuario.setId(id);
            Usuario usuarioAntigo = recuperar(id);
            usuarioAntigo.setNome(usuario.getNome());
            usuarioAntigo.setLogin(usuario.getLogin());
            usuarioAntigo.setSenha(usuario.getSenha());
            usuarioAntigo.setEmail(usuario.getEmail());
            usuarioAntigo.setPermissoes(usuario.getPermissoes());
            usuarioAntigo.setAtivo(usuario.isAtivo());
           return usuarioDAO.save(usuario);
        }else{
            throw new NaoEncontrado("USUÁRIO não encontrado");
        }
    }
    
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void apagar(@PathVariable long id){
        if (usuarioDAO.existsById(id)){
            usuarioDAO.deleteById(id);
        }else {
            throw new NaoEncontrado("USUÁRIO não encontrado");
        }
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
}
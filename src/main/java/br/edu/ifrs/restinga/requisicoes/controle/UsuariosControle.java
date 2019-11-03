package br.edu.ifrs.restinga.requisicoes.controle;

import br.edu.ifrs.restinga.requisicoes.autenticacao.MeuUser;
import br.edu.ifrs.restinga.requisicoes.dao.UsuarioDAO;
import br.edu.ifrs.restinga.requisicoes.erros.NaoEncontrado;
import br.edu.ifrs.restinga.requisicoes.erros.Proibido;
import br.edu.ifrs.restinga.requisicoes.erros.RequisicaoInvalida;
import br.edu.ifrs.restinga.requisicoes.modelo.Aluno;
import br.edu.ifrs.restinga.requisicoes.modelo.Professor;
import br.edu.ifrs.restinga.requisicoes.modelo.Servidor;
import br.edu.ifrs.restinga.requisicoes.modelo.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping(path = "/api/usuarios")
public class UsuariosControle {
    
    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
    
    @Autowired
    UsuarioDAO usuarioDAO;
    
    private void validaUsuario(Usuario u) {
        
        if (u.getEmail() == null || u.getEmail().isEmpty()) {
            throw new RequisicaoInvalida("email é obrigatórios");
        }
        if (u.getLogin() == null || u.getLogin().isEmpty()) {
            throw new RequisicaoInvalida("login é obrigatórios");
        }
        if (u.getNome() == null || u.getNome().isEmpty()) {
            throw new RequisicaoInvalida("nome é obrigatórios");
        }
        if (u.getNovaSenha() == null || u.getNovaSenha().isEmpty()) {
            throw new RequisicaoInvalida("senha e obrigatorio é obrigatórios");
        }
        if (u.getPermissoes() == null || u.getPermissoes().isEmpty()) {
            throw new RequisicaoInvalida("permissão é obrigatórios");
        }
        if (u instanceof Aluno) {
            if (((Aluno) u).getDataIngresso() == null ) {
                throw new RequisicaoInvalida("o campo data é obrigatórios");
            }
            if (((Aluno) u).getMatricula() <= 0) {
                throw new RequisicaoInvalida("matricula é obrigatórios");
            }
        } 
        if (u instanceof Servidor) {
            if (((Servidor) u).getCargo() == null || ((Servidor) u).getCargo().isEmpty()) {
                throw new RequisicaoInvalida("cargo é obrigatórios");
            }
            if (((Servidor) u).getSiape() <= 0) {
                throw new RequisicaoInvalida("siape é obrigatórios");
            }
         }
        if (u instanceof Professor) {
            if (((Professor) u).getSiape() <=0) {
                throw new RequisicaoInvalida("siape é obrigatórios");
            }
        }
    }
///////////// LISTAR USUÁRIOS ////////////////////////       



   // @PreAuthorize("hasAuthority('ensino')")
    @RequestMapping(path = "/", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Usuario> listar(@AuthenticationPrincipal MeuUser usuarioAutenticado) {
        if (usuarioAutenticado.getUsuario().getPermissoes().contains("ensino")) {
            return usuarioDAO.findAll();
        }
        throw new Proibido("não e permitido acessar dados de outros usuarios");
    }
    
    //inserir ensino e professor
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario inserirEnsinoProfessor(@RequestBody Usuario usuario){
        usuario.setSenha(PASSWORD_ENCODER.encode(usuario.getNovaSenha()));
        Usuario usuarioBanco = usuarioDAO.findByLogin(usuario.getLogin());
        if (usuarioBanco != null) {
            throw new RequisicaoInvalida("este login ja existe");
        }
        validaUsuario(usuario);
        if (usuario instanceof Aluno) {
           throw new Proibido("não e permitido adicionar um aluno");
        }else if(!usuario.getPermissoes().contains("aluno")){
            return usuarioDAO.save(usuario);
        }
        throw new Proibido("não e permitido adicionar um aluno");
    }

    //inserir aluno
    @PostMapping("/aluno/")
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario novoUsuario(@RequestBody Usuario usuario) {
        usuario.setSenha(PASSWORD_ENCODER.encode(usuario.getNovaSenha()));
        usuario.setPermissoes("aluno");
        Usuario usuarioBanco = usuarioDAO.findByLogin(usuario.getLogin());
        if (usuarioBanco != null) {
            throw new RequisicaoInvalida("este login ja existe");
        }
        validaUsuario(usuario);
        if (usuario instanceof Servidor) {
            throw new Proibido("não e permitido adicionar um servidor");
        }
        if (usuario instanceof Professor) {
           throw new Proibido("não e permitido adicionar um professor");
        }
        return usuarioDAO.save(usuario);
    }

    // recuperar o usuario pela id
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Usuario recuperar(@AuthenticationPrincipal MeuUser usuarioAutenticado, @PathVariable long id) {
        if (usuarioAutenticado.getUsuario().getId() == id
                || usuarioAutenticado.getUsuario().getPermissoes().contains("ensino")) {
            Optional<Usuario> findById = usuarioDAO.findById(id);
            if (findById.isPresent()) {
                return findById.get();
            } else {
                throw new NaoEncontrado("USUÁRIO não encontrado");
            }
        }
        throw new Proibido("não e permitido acessar dados de outros usuarios");
        
    }

    // atualizar os usuarios
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario atualizar(@AuthenticationPrincipal MeuUser usuarioAutenticado,
        @PathVariable long id, @RequestBody Usuario usuario) {
        validaUsuario(usuario);
        if (usuarioAutenticado.getUsuario().getPermissoes().contains("ensino") 
                || usuarioAutenticado.getUsuario().getId() == id) {
            if (usuarioDAO.existsById(id)) {
                
                Usuario usuarioAntigo = recuperar(usuarioAutenticado, id);
                usuarioAntigo.setNome(usuario.getNome());
                usuarioAntigo.setLogin(usuario.getLogin());
                usuarioAntigo.setSenha(usuario.getNovaSenha());
                usuarioAntigo.setEmail(usuario.getEmail());
                usuarioAntigo.setPermissoes(usuario.getPermissoes());
                usuarioAntigo.setAtivo(usuario.isAtivo());
                //fiz esses if para saber em que estancia esta a classe e atualizar campos especificos de cada classe
                //feito potr joao
                if (usuarioAntigo instanceof Servidor) {
                    if (usuario instanceof Servidor) {
                        ((Servidor) usuarioAntigo).setCargo(((Servidor) usuario).getCargo());
                        ((Servidor) usuarioAntigo).setSiape(((Servidor) usuario).getSiape());
                    }
                    
                }
                if (usuarioAntigo instanceof Aluno) {
                    if (usuario instanceof Aluno) {
                        ((Aluno) usuarioAntigo).setDataIngresso(((Aluno) usuario).getDataIngresso());
                        ((Aluno) usuarioAntigo).setMatricula(((Aluno) usuario).getMatricula());
                    }
                }
                if (usuarioAntigo instanceof Professor) {
                    if (usuario instanceof Professor) {
                        ((Professor) usuarioAntigo).setSiape(((Professor) usuario).getSiape());
                        ((Professor) usuarioAntigo).setCoordenador(((Professor) usuario).isCoordenador());
                    }
                }
                return usuarioDAO.save(usuario);
            } else {
                throw new NaoEncontrado("USUÁRIO não encontrado");
            }
        }
        throw new Proibido(" não e permitido alterar dados de outros usuarios");
    }
    
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public List<Usuario> apagar(@AuthenticationPrincipal MeuUser usuarioAutenticado,@PathVariable long id) {
        if (usuarioAutenticado.getUsuario().getPermissoes().contains("ensino")) {
            if (usuarioDAO.existsById(id)) {
                usuarioDAO.deleteById(id);
               return usuarioDAO.findAll();
            } else {
                throw new NaoEncontrado("USUÁRIO não encontrado");
            }
        }
        throw new Proibido("não e permitido apagar outros usuários");
       
    }

    //login normal so com usuario e senha para autenticação
    @RequestMapping(path = "/usuarios/login/", method = RequestMethod.GET)
    public Usuario login(@RequestParam String usuario,
            @RequestParam String senha) {
        Usuario usuarioBanco = usuarioDAO.findByLogin(usuario);
        if (usuarioBanco != null) {
            boolean matches
                    = PASSWORD_ENCODER.matches(senha, usuarioBanco.getSenha());
            if (matches) {
                return usuarioBanco;
            }
        }
        throw new NaoEncontrado("Usuário e/ou senha incorreto(s)");
    }

    // este seria o login por token que depois de um certo tempo precisa se logar novamente ao sistema
    public static final String SEGREDO = "string grande ";
    
    @RequestMapping(path = "/usuarios/loginOld/", method = RequestMethod.GET)
    public ResponseEntity<Usuario> loginToken(@RequestParam String usuario,
            @RequestParam String senha) throws UnsupportedEncodingException {
        
        Usuario usuarioBanco = usuarioDAO.findByLogin(usuario);
        if (usuarioBanco != null) {
            boolean achou
                    = PASSWORD_ENCODER.matches(senha, usuarioBanco.getSenha());
            if (achou) {

                // aqui podemos fazer com chave publica e privada se quiser que fique mais seguro o token
                Algorithm algorithm = Algorithm.HMAC512(SEGREDO);
                Calendar agora = Calendar.getInstance();
                agora.add(Calendar.MINUTE, 30);
                Date expira = agora.getTime();
                String token = JWT.create()
                        .withClaim("id", usuarioBanco.getId()).
                        withExpiresAt(expira).
                        sign(algorithm);
                HttpHeaders respHeaders = new HttpHeaders();
                respHeaders.set("token", token);
                return new ResponseEntity<>(usuarioBanco,
                        respHeaders, HttpStatus.OK);
            }
        }
        throw new NaoEncontrado("Usuário e/ou senha incorreto(s)");
    }
}

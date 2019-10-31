package br.edu.ifrs.restinga.requisicoes.controle;

import br.edu.ifrs.restinga.requisicoes.autenticacao.MeuUser;
import br.edu.ifrs.restinga.requisicoes.dao.RequisicaoDAO;
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
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    
    @Autowired
    RequisicaoDAO requisicaoDAO;
    
    private void validaUsuario(Usuario u) {
        if (u instanceof Aluno) {
            if (((Aluno) u).getDataIngresso().equals("")
                    || u.getNome() == null || u.getNome().isEmpty()
                    || u.getLogin() == null || u.getLogin().isEmpty()
                    || ((Aluno) u).getMatricula() <= 0 
                    || ((Aluno) u).getDataIngresso() == null 
                    ||  u.getNovaSenha() == null || u.getNovaSenha().isEmpty()) {
                throw new RequisicaoInvalida("Todos os campos são obrigatórios");
            }
        } else if (u instanceof Servidor) {
            if (u.getNome() == null || u.getNome().isEmpty()
                    || u.getLogin() == null || u.getLogin().isEmpty()
                    || u.getNovaSenha() == null || u.getNovaSenha().isEmpty()
                    || u.getEmail() == null || u.getEmail().isEmpty()
                    || u.getPermissoes() == null || u.getPermissoes().isEmpty()) {
                throw new RequisicaoInvalida("Todos os campos são obrigatórios");
            }
        } else if (u instanceof Professor) {
            if (u.getNome() == null || u.getNome().isEmpty()
                    || u.getLogin() == null || u.getLogin().isEmpty()
                    || u.getNovaSenha() == null || u.getNovaSenha().isEmpty()
                    || u.getEmail() == null || u.getEmail().isEmpty()
                    || u.getPermissoes() == null || u.getPermissoes().isEmpty()
                    || ((Professor) u).getSiape() <= 0) {
                throw new RequisicaoInvalida("Todos os campos são obrigatórios");
            }
        }
    }
///////////// LISTAR USUÁRIOS ////////////////////////       



    @PreAuthorize("hasAuthority('servidor')")
    @RequestMapping(path = "/", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Usuario> listar(@AuthenticationPrincipal MeuUser usuarioAutenticado) {
        if (usuarioAutenticado.getUsuario().getPermissoes().contains("servidor")) {
            return usuarioDAO.findAll();
        }
        throw new Proibido("não e permitido acessar dados de outros usuarios");
    }

    //inserir usuario
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario novoUsuario(@AuthenticationPrincipal MeuUser usuarioAutenticado,
            @RequestBody Usuario usuario) {
        validaUsuario(usuario);
        usuario.setSenha(PASSWORD_ENCODER.encode(usuario.getNovaSenha()));
        if (usuarioAutenticado == null || !usuarioAutenticado.getUsuario().getPermissoes().contains("servidor")
                || !usuarioAutenticado.getUsuario().getPermissoes().contains("professor")) {
            usuario.setPermissoes("aluno");
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
        if (usuarioAutenticado.getUsuario().getPermissoes().contains("ensino")) {
            if (usuarioDAO.existsById(id)) {
                usuario.setId(id);
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
    public void apagar(@PathVariable long id) {
        if (usuarioDAO.existsById(id)) {
            usuarioDAO.deleteById(id);
        } else {
            throw new NaoEncontrado("USUÁRIO não encontrado");
        }
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

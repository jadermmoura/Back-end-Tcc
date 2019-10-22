
package br.edu.ifrs.restinga.requisicoes.autenticacao;


import br.edu.ifrs.restinga.requisicoes.modelo.Usuario;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class MeuUser extends User {
    private final Usuario usuario;
    public MeuUser(Usuario usuario) {
        super(usuario.getLogin(),
                usuario.getSenha(),
                AuthorityUtils.createAuthorityList(
                    usuario.getPermissoes()));
        this.usuario=usuario;
    }
    public Usuario getUsuario() {
        return usuario;
    }
}

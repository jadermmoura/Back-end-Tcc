/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.requisicoes.autenticacao;




import br.edu.ifrs.restinga.requisicoes.ConfiguracaoSeguranca;
import br.edu.ifrs.restinga.requisicoes.dao.UsuarioDAO;
import br.edu.ifrs.restinga.requisicoes.modelo.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;


public class FiltroPorToken 
        extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
            HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String token;
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
        } else {
            token = request.getParameter("token");
        }
        if (token != null && !token.isEmpty()) {
            Algorithm algorithm = Algorithm.HMAC512(ConfiguracaoSeguranca.SEGREDO);
            DecodedJWT decode = JWT.require(algorithm).build().verify(token);
            Integer id = decode.getClaim("id").asInt();
            Usuario usuario = usuarioDAO.findById(id).get();
            MeuUser usuarioAut = new MeuUser(usuario);
            UsernamePasswordAuthenticationToken authToken = 
                    new UsernamePasswordAuthenticationToken(usuarioAut
                            , null, usuarioAut.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        chain.doFilter(request, response);
    }
    UsuarioDAO usuarioDAO;

    public FiltroPorToken(UsuarioDAO usuarioDAO) {
        super();
        this.usuarioDAO = usuarioDAO;

    }

}

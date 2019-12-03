/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.requisicoes;



import static br.edu.ifrs.restinga.requisicoes.ConfiguracaoSeguranca.PASSWORD_ENCODER;
import br.edu.ifrs.restinga.requisicoes.dao.ServidorDAO;
import br.edu.ifrs.restinga.requisicoes.dao.UsuarioDAO;
import br.edu.ifrs.restinga.requisicoes.modelo.Servidor;
import br.edu.ifrs.restinga.requisicoes.modelo.Usuario;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Xinicializador {
    @Autowired
    UsuarioDAO usuarioDAO;
    @Autowired
    ServidorDAO servidorDAO;
    // Executa o método logo após a aplicação spring inicializar por completo 
    @PostConstruct
    public void init() {
        Usuario usuarios = usuarioDAO.findByLogin("admin");
        if (usuarios == null) {
            Servidor usuario = new Servidor();
            usuario.setNome("admin");
            usuario.setLogin("admin");
            usuario.setSenha(PASSWORD_ENCODER.encode("12345"));
            usuario.setEmail("admin@admin");
            usuario.setPermissoes("ensino");
            usuario.setSiape(10070378);
            usuario.setCargo("servidor");
            usuario.setAtivo(true);
            servidorDAO.save(usuario);
        //coloca um usuario inicial no banco quando iniciar a aplicação
        }
          
        
       
    }
}

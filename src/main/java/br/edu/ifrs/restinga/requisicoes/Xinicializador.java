/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.requisicoes;

import static br.edu.ifrs.restinga.requisicoes.controle.UsuariosControle.PASSWORD_ENCODER;
import br.edu.ifrs.restinga.requisicoes.dao.UsuarioDAO;
import br.edu.ifrs.restinga.requisicoes.modelo.Usuario;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author 10070267
 */
//@Configuration

public class Xinicializador {
    @Autowired
    UsuarioDAO usuarioDAO;
 @PostConstruct
    public void init() {
        Usuario usuarioRoot = usuarioDAO.findByLogin("admin");
        if (usuarioRoot == null) {
            usuarioRoot = new Usuario() {};
            usuarioRoot.setNome("admin");
            usuarioRoot.setLogin("admin");
            usuarioRoot.setSenha(PASSWORD_ENCODER.encode("12345"));
            usuarioRoot.setEmail("admin@admin");
            usuarioRoot.setPermissoes("servidor");
            usuarioRoot.setAtivo(true);
            usuarioDAO.save(usuarioRoot);
     }
    }
    
}

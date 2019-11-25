package br.edu.ifrs.restinga.requisicoes;

import static br.edu.ifrs.restinga.requisicoes.controle.UsuariosControle.PASSWORD_ENCODER;
import br.edu.ifrs.restinga.requisicoes.dao.ServidorDAO;
import br.edu.ifrs.restinga.requisicoes.modelo.Servidor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
@EnableAutoConfiguration
public class RequisicoesApplication {

	public static void main(String[] args) {
            ApplicationContext ctx = SpringApplication.run(RequisicoesApplication.class);
            Servidor usuario = new Servidor();
            usuario.setNome("admin");
            usuario.setLogin("admin");
            usuario.setSenha(PASSWORD_ENCODER.encode("12345"));
            usuario.setEmail("admin@admin");
            usuario.setPermissoes("ensino");
            usuario.setSiape(10070378);
            usuario.setCargo("servidor");
            usuario.setAtivo(true);
            ServidorDAO repository = ctx.getBean(ServidorDAO.class);
            repository.save(usuario);
            
           
	}

}

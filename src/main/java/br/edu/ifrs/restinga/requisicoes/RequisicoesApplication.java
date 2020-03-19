package br.edu.ifrs.restinga.requisicoes;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@ComponentScan
@EnableAutoConfiguration
public class RequisicoesApplication implements CommandLineRunner{

    public static void main(String[] args) {
        SpringApplication.run(RequisicoesApplication.class,args);
    }

    @Override
    public void run(String... args) throws Exception {
        String pass = new BCryptPasswordEncoder().encode("12345");
        System.out.println("Senha : "+pass);
    }
}

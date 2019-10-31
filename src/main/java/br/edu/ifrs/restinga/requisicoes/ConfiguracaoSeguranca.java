/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.requisicoes;


import br.edu.ifrs.restinga.requisicoes.autenticacao.MeuUserDetailsService;
import br.edu.ifrs.restinga.requisicoes.dao.UsuarioDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


import org.springframework.stereotype.Component;


@Component
@EnableWebS
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class ConfiguracaoSeguranca extends WebSecurityConfigurerAdapter {

    @Autowired
    MeuUserDetailsService detailsService;
    
    @Autowired
    UsuarioDAO usuarioDAO;

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(detailsService)
                .passwordEncoder(Usuarios.PASSWORD_ENCODER);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //o GET login pode ser acessado sem autenticação 
                .antMatchers(HttpMethod.GET, "/api/usuarios/login/").permitAll()
                // Caso o sistema permita o autocadastro                
                .antMatchers(HttpMethod.POST, "/api/usuarios/").permitAll()
                // permite o acesso somente se autenticado
                .antMatchers("/api/**").authenticated()
                .and().httpBasic()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().csrf().disable();
    }
}

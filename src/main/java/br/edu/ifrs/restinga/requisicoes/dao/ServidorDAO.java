/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.requisicoes.dao;

import br.edu.ifrs.restinga.requisicoes.modelo.Servidor;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ServidorDAO extends JpaRepository<Servidor, Long>{  
}

package br.edu.ifrs.restinga.requisicoes.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifrs.restinga.requisicoes.modelo.Anexo;

@Repository
public interface AnexoDAO extends JpaRepository<Anexo, Long> {
    
}
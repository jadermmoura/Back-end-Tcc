package br.edu.ifrs.restinga.requisicoes.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifrs.restinga.requisicoes.modelo.Disciplina;
@Repository
public interface DisciplinaDAO extends JpaRepository<Disciplina, Long> {
	

}

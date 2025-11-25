package com.jtarcio.portfolioapi.repository;

import com.jtarcio.portfolioapi.model.entity.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjetoRepository extends JpaRepository<Projeto, Long> {
}

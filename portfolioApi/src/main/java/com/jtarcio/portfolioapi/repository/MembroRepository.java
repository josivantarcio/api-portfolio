package com.jtarcio.portfolioapi.repository;

import com.jtarcio.portfolioapi.model.entity.Membro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembroRepository extends JpaRepository<Membro, Long> {
}

package com.alanpatrik.sghss.api.repository;

import com.alanpatrik.sghss.api.model.Prontuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProntuarioRepository extends JpaRepository<Prontuario, Long> {
}

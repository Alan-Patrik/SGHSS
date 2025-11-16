package com.alanpatrik.sghss.api.repository;

import com.alanpatrik.sghss.api.model.ProfissionalSaude;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfissionalSaudeRepository extends JpaRepository<ProfissionalSaude, Long> {
    boolean existsProfissionalSaudeByNome(String nome);

    boolean existsProfissionalSaudeByCRM(String nome);

    Optional<ProfissionalSaude> findByCRM(String nome);

}

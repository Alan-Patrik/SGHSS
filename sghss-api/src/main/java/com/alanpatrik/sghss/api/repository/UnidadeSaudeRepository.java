package com.alanpatrik.sghss.api.repository;

import com.alanpatrik.sghss.api.model.UnidadeSaude;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UnidadeSaudeRepository extends JpaRepository<UnidadeSaude, Long> {
    boolean existsUnidadeSaudeByNome(String nome);

    Optional<UnidadeSaude> findByNome(String nome);
}

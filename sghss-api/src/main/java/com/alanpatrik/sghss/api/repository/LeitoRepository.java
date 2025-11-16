package com.alanpatrik.sghss.api.repository;

import com.alanpatrik.sghss.api.model.Leito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeitoRepository extends JpaRepository<Leito, Long> {
    boolean existsLeitoByNumero(String numero);
}

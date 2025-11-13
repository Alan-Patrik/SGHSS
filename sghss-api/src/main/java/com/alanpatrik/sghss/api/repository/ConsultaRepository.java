package com.alanpatrik.sghss.api.repository;

import com.alanpatrik.sghss.api.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
}

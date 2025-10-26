package com.alanpatrik.sghss.api.repository;

import com.alanpatrik.sghss.api.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    boolean existsPacienteByNome(String nome);
}

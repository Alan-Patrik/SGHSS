package com.alanpatrik.sghss.api.repository;

import com.alanpatrik.sghss.api.model.Exame;
import com.alanpatrik.sghss.api.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExameRepository extends JpaRepository<Exame, Long> {
    List<Exame> findByPaciente(Paciente paciente);
}

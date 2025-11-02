package com.alanpatrik.sghss.api.repository;

import com.alanpatrik.sghss.api.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    boolean existsPacienteByNome(String nome);

    @Query(value = "SELECT pa.* FROM paciente pa " +
            "LEFT JOIN prontuario pr ON pr.id_paciente = pa.id_paciente " +
            "WHERE pa.id_paciente = :id", nativeQuery = true)
    Paciente findByHistoricoClinico(@Param("id") Long id);

}

package com.alanpatrik.sghss.api.repository;

import com.alanpatrik.sghss.api.model.Prontuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProntuarioRepository extends JpaRepository<Prontuario, Long> {
    @Query(value = "SELECT p.* FROM prontuario p " +
            "LEFT JOIN prescricao pr ON pr.id_prescricao = p.id_prontuario " +
            "WHERE p.id_prontuario = :id", nativeQuery = true)
    Prontuario findByPrescricoes(@Param("id") Long id);
}

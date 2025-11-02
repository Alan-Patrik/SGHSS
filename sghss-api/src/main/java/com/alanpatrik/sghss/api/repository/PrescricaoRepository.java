package com.alanpatrik.sghss.api.repository;

import com.alanpatrik.sghss.api.model.Prescricao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrescricaoRepository extends JpaRepository<Prescricao, Long> {
    boolean existsPrescricaoByMedicamento(String medicamento);

    Prescricao findByMedicamento(String medicamento);
}

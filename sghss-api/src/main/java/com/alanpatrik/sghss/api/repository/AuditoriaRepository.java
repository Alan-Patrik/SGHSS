package com.alanpatrik.sghss.api.repository;

import com.alanpatrik.sghss.api.model.Auditoria;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Long>, AuditoriaRepositoryCustom {

    @Query("select a from Auditoria a where a.id = :id")
    Optional<Auditoria> findOneSimple(@Param("id") Long id);

    @Query("select a from Auditoria a order by a.eventTime desc")
    List<Auditoria> findTopN(Pageable pageable);
}

package com.alanpatrik.sghss.api.repository;

import com.alanpatrik.sghss.api.model.Auditoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AuditoriaRepositoryCustom {
    Page<Auditoria> search(String username, String action, String resource,
                           LocalDateTime from, LocalDateTime to,
                           Pageable pageable);
}

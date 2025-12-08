package com.alanpatrik.sghss.api.repository;

import com.alanpatrik.sghss.api.model.UsuarioRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRoleRepository extends JpaRepository<UsuarioRole, Long> {

    @Query("""
                select ur from UsuarioRole ur
                join ur.usuario u
                where u.username = :username
            """)
    List<UsuarioRole> findByUsuarioUsername(@Param("username") String username);
}


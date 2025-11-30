package com.alanpatrik.sghss.api.repository;

import com.alanpatrik.sghss.api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByEmail(String email);

    @Query("""
                select distinct u from Usuario u
                left join fetch u.usuarioRoles ur
                left join fetch ur.role r
                left join fetch r.rolePrivilegios rp
                left join fetch rp.privilegio p
                where u.username = :username
            """)
    Optional<Usuario> loadUserGraph(@Param("username") String username);
}

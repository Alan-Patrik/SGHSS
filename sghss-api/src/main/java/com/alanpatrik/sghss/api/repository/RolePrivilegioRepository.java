package com.alanpatrik.sghss.api.repository;

import com.alanpatrik.sghss.api.model.RolePrivilegio;
import com.alanpatrik.sghss.api.model.RolePrivilegioId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePrivilegioRepository extends JpaRepository<RolePrivilegio, RolePrivilegioId> {

    @Query("""
                select rp from RolePrivilegio rp
                join rp.role r
                where r.authority = :authority
            """)
    List<RolePrivilegio> findByRoleAuthority(@Param("authority") String authority);
}

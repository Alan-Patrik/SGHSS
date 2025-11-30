package com.alanpatrik.sghss.api.model;

import com.alanpatrik.sghss.api.model.dto.response.RoleResponseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ROLE")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ROLE")
    private Long id;

    @Column(name = "TXT_AUTHORITY", nullable = false, unique = true, length = 100)
    private String authority;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RolePrivilegio> rolePrivilegios = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private Set<UsuarioRole> usuarioRoles = new HashSet<>();

    public static RoleResponseDTO toResponseDTO(Role role) {
        var roleDTO = new RoleResponseDTO();
        roleDTO.setId(role.getId());
        roleDTO.setAuthority(role.getAuthority());
        roleDTO.setRolePrivilegios(Collections.singleton(role.getRolePrivilegios().toString()));
        return roleDTO;
    }

    @Transient
    public Set<Privilegio> getPrivilegios() {
        Set<Privilegio> set = new HashSet<>();
        for (var rolePrivilegio : rolePrivilegios) {
            set.add(rolePrivilegio.getPrivilegio());
        }
        return set;
    }
}


package com.alanpatrik.sghss.api.model;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class UsuarioRoleId implements Serializable {

    @Column(name = "USUARIO")
    private Long usuarioId;

    @Column(name = "ROLE")
    private Long roleId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsuarioRoleId that)) return false;
        return Objects.equals(usuarioId, that.usuarioId) &&
                Objects.equals(roleId, that.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuarioId, roleId);
    }
}

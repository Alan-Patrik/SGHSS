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
public class RolePrivilegioId implements Serializable {

    @Column(name = "ID_ROLE")
    private Long roleId;

    @Column(name = "ID_PRIVILEGE")
    private Long privilegioId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RolePrivilegioId that)) return false;
        return Objects.equals(roleId, that.roleId) &&
                Objects.equals(privilegioId, that.privilegioId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, privilegioId);
    }
}


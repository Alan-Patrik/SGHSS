package com.alanpatrik.sghss.api.model;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ROLE_PRIVILEGIO")
public class RolePrivilegio {

    @EmbeddedId
    private RolePrivilegioId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleId")
    @JoinColumn(name = "ID_ROLE")
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("privilegioId")
    @JoinColumn(name = "ID_PRIVILEGIO")
    private Privilegio privilegio;

}


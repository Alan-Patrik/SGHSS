package com.alanpatrik.sghss.api.model.dto.response;

import com.alanpatrik.sghss.api.model.UsuarioRole;
import lombok.*;

import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UsuarioResponseDTO {

    private Long id;
    private String username;
    private String password;
    private String email;
    private Set<UsuarioRole> usuarioRoles;
}

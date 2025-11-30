package com.alanpatrik.sghss.api.model.dto.response;

import com.alanpatrik.sghss.api.model.RolePrivilegio;
import lombok.*;

import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoleResponseDTO {

    private Long id;
    private String authority;
    private Set<String> rolePrivilegios;
}


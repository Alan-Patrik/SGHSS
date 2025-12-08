package com.alanpatrik.sghss.api.model.dto.request;

import com.alanpatrik.sghss.api.model.RolePrivilegio;
import lombok.*;

import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoleRequestDTO {

    private String authority;
    private Set<RolePrivilegio> rolePrivilegios;
}


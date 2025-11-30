package com.alanpatrik.sghss.api.model.dto.request;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UsuarioUpdateRequestDTO {

    private String username;
    private String password;
    private String email;
}

package com.alanpatrik.sghss.api.model.dto.request;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UsuarioRequestDTO {

    private String username;
    private String password;
    private String email;
}

package com.alanpatrik.sghss.api.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProfissionalSaudeResponseDTO {
    private Long id;
    private String nome;
    private String CRM;
}

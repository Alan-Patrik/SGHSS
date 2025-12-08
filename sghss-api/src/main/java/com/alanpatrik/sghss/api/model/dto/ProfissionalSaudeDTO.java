package com.alanpatrik.sghss.api.model.dto;

import com.alanpatrik.sghss.api.model.Endereco;
import com.alanpatrik.sghss.api.model.enums.AreaAtuacao;
import com.alanpatrik.sghss.api.model.enums.Especialidade;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProfissionalSaudeDTO {
    private Long id;
    private String nome;
    private String cpf;
    private String dataNascimento;
    private String telefone;
    private String email;
    private Endereco endereco;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataModificacao;
    private Especialidade especialidade;
    private AreaAtuacao areaAtuacao;
    private Set<ConsultaDTO> consultas;
    private String CRM;
}

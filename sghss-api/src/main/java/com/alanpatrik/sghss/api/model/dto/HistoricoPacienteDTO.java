package com.alanpatrik.sghss.api.model.dto;

import com.alanpatrik.sghss.api.model.Endereco;
import com.alanpatrik.sghss.api.model.dto.response.ProntuarioResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class HistoricoPacienteDTO {

    private Long id;
    private String nome;
    private String cpf;
    private String dataNascimento;
    private Endereco endereco;
    private String telefone;
    private String email;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataModificacao;
    private ProntuarioResponseDTO prontuario;
    private Set<ExameDTO> exames;
}

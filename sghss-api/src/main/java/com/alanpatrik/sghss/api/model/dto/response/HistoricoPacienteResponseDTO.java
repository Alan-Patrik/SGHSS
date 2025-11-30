package com.alanpatrik.sghss.api.model.dto.response;

import com.alanpatrik.sghss.api.model.Endereco;
import com.alanpatrik.sghss.api.model.Exame;
import com.alanpatrik.sghss.api.model.Prontuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class HistoricoPacienteResponseDTO {

    private Long id;
    private String nome;
    private String cpf;
    private String dataNascimento;
    private Endereco endereco;
    private String telefone;
    private String email;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataModificacao;
    private Prontuario prontuario;
    private List<Exame> exames;
}

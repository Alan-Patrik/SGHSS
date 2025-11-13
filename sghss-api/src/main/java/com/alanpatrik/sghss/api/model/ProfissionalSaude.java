package com.alanpatrik.sghss.api.model;

import com.alanpatrik.sghss.api.model.dto.response.ProfissionalSaudeResponseDTO;
import com.alanpatrik.sghss.api.model.enums.Especialidade;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PROFISSIONAL_SAUDE")
public class ProfissionalSaude extends Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PROFISSIONAL_SAUDE", nullable = false)
    private Long id;

    @Column(name = "INDI_TXT_ESPECIALIDADE", nullable = false)
    @Enumerated(EnumType.STRING)
    private Especialidade especialidade;

    @Column(name = "TXT_CRM", nullable = false)
    private String CRM;

    @OneToOne(mappedBy = "profissionalSaude")
    @JsonIgnore
    private Agenda agenda;

    public ProfissionalSaude(
            String nome,
            String cpf,
            String dataNascimento,
            String telefone,
            String email,
            Endereco endereco,
            LocalDateTime dataCriacao,
            LocalDateTime dataModificacao,
            Especialidade especialidade,
            String CRM,
            Agenda agenda) {
        super(nome, cpf, dataNascimento, telefone, email, endereco, dataCriacao, dataModificacao);
        this.especialidade = especialidade;
        this.CRM = CRM;
        this.agenda = agenda;
    }

    public static ProfissionalSaudeResponseDTO toResponseDTO(ProfissionalSaude profissionalSaude) {
        var profissionalSaudeResponseDTO = new ProfissionalSaudeResponseDTO();
        profissionalSaudeResponseDTO.setId(profissionalSaude.getId());
        profissionalSaudeResponseDTO.setNome(profissionalSaude.getNome());
        profissionalSaudeResponseDTO.setCpf(profissionalSaude.getCpf());
        profissionalSaudeResponseDTO.setDataNascimento(profissionalSaude.getDataNascimento());
        profissionalSaudeResponseDTO.setTelefone(profissionalSaude.getTelefone());
        profissionalSaudeResponseDTO.setEmail(profissionalSaude.getEmail());
        profissionalSaudeResponseDTO.setEndereco(profissionalSaude.getEndereco());
        profissionalSaudeResponseDTO.setDataCriacao(profissionalSaude.getDataCriacao());
        profissionalSaudeResponseDTO.setDataModificacao(profissionalSaude.getDataModificacao());
        profissionalSaudeResponseDTO.setEspecialidade(profissionalSaude.getEspecialidade());
        profissionalSaudeResponseDTO.setCRM(profissionalSaude.getCRM());
        return profissionalSaudeResponseDTO;
    }

    public static ProfissionalSaude toEntity(ProfissionalSaudeResponseDTO profissionalSaudeResponseDTO) {
        var profissionalSaude = new ProfissionalSaude();
        profissionalSaude.setId(profissionalSaudeResponseDTO.getId());
        profissionalSaude.setNome(profissionalSaudeResponseDTO.getNome());
        profissionalSaude.setCpf(profissionalSaudeResponseDTO.getCpf());
        profissionalSaude.setDataNascimento(profissionalSaudeResponseDTO.getDataNascimento());
        profissionalSaude.setTelefone(profissionalSaudeResponseDTO.getTelefone());
        profissionalSaude.setEmail(profissionalSaudeResponseDTO.getEmail());
        profissionalSaude.setEndereco(profissionalSaudeResponseDTO.getEndereco());
        profissionalSaude.setDataCriacao(profissionalSaudeResponseDTO.getDataCriacao());
        profissionalSaude.setDataModificacao(profissionalSaudeResponseDTO.getDataModificacao());
        profissionalSaude.setEspecialidade(profissionalSaudeResponseDTO.getEspecialidade());
        profissionalSaude.setCRM(profissionalSaudeResponseDTO.getCRM());
        return profissionalSaude;
    }
//
//    public void atualizarProntuario() {
//
//    }
//
//    public void emitirReceita() {
//
//    }
//
//    public void consultarHistoricoPaciente() {
//
//    }
}

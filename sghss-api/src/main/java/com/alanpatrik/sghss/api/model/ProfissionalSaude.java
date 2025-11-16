package com.alanpatrik.sghss.api.model;

import com.alanpatrik.sghss.api.model.dto.response.ProfissionalSaudeResponseDTO;
import com.alanpatrik.sghss.api.model.enums.AreaAtuacao;
import com.alanpatrik.sghss.api.model.enums.Especialidade;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "AREA_ATUACAO", nullable = false)
    private AreaAtuacao areaAtuacao;

    @Column(name = "TXT_CRM", nullable = false, unique = true)
    private String CRM;

    @OneToMany(mappedBy = "profissionalSaude")
    @JsonIgnore
    private List<Consulta> consultas;

    @OneToOne(mappedBy = "profissionalSaude")
    @JsonIgnore
    private Agenda agenda;

    @ManyToOne
    @JsonIgnore
    private UnidadeSaude unidadeSaude;

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
            AreaAtuacao areaAtuacao,
            List<Consulta> consultas,
            String CRM,
            Agenda agenda,
            UnidadeSaude unidadeSaude) {
        super(nome, cpf, dataNascimento, telefone, email, endereco, dataCriacao, dataModificacao);
        this.especialidade = especialidade;
        this.areaAtuacao = areaAtuacao;
        this.consultas = consultas;
        this.CRM = CRM;
        this.agenda = agenda;
        this.unidadeSaude = unidadeSaude;
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
        profissionalSaudeResponseDTO.setAreaAtuacao(profissionalSaude.getAreaAtuacao());
        profissionalSaudeResponseDTO.setConsultas(Consulta.responseToDTOList(profissionalSaude.getConsultas()));
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
        profissionalSaude.setAreaAtuacao(profissionalSaudeResponseDTO.getAreaAtuacao());
        profissionalSaude.setConsultas(profissionalSaude.getConsultas());
        profissionalSaude.setCRM(profissionalSaudeResponseDTO.getCRM());
        return profissionalSaude;
    }

    public static List<ProfissionalSaudeResponseDTO> toResponseDTOList(List<ProfissionalSaude> profissionaisSaude) {
        var profissionaisSaudeResponseDTO = new ArrayList<ProfissionalSaudeResponseDTO>();
        for (var profissionalSaude : profissionaisSaude) {
            profissionaisSaudeResponseDTO.add(toResponseDTO(profissionalSaude));
        }
        return profissionaisSaudeResponseDTO;
    }

    public static List<ProfissionalSaude> toEntityList(List<ProfissionalSaudeResponseDTO> profissionaisSaudeDTO) {
        var profissionaisSaude = new ArrayList<ProfissionalSaude>();
        for (var profissionalSaudeDTO : profissionaisSaudeDTO) {
            profissionaisSaude.add(ProfissionalSaude.toEntity(profissionalSaudeDTO));
        }
        return profissionaisSaude;
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

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

    @Column(name = "TXT_CRM", nullable = false)
    private String CRM;

    @OneToMany(mappedBy = "profissionalSaude")
    @JsonIgnore
    private List<Consulta> consultas;

    @OneToOne(mappedBy = "profissionalSaude")
    @JsonIgnore
    private Agenda agenda;

    @ManyToOne
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
        profissionalSaudeResponseDTO.setUnidadeSaude(profissionalSaude.getUnidadeSaude());
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
        profissionalSaude.setUnidadeSaude(profissionalSaudeResponseDTO.getUnidadeSaude());
        return profissionalSaude;
    }

    public static List<ProfissionalSaudeResponseDTO> responseToResponseDTOList(List<ProfissionalSaude> profissionalSaudeList) {
        var profissionalSaudeResponseDTOList = new ArrayList<ProfissionalSaudeResponseDTO>();
        for (var profissionalSaude : profissionalSaudeList) {
            var profissionalSaudeResponseDTO = toResponseDTO(profissionalSaude);
            profissionalSaudeResponseDTOList.add(profissionalSaudeResponseDTO);
        }
        return profissionalSaudeResponseDTOList;
    }

    public static List<ProfissionalSaude> responseToEntityList(List<ProfissionalSaudeResponseDTO> profissionalSaudeResponseDTOList) {
        var profissionalSaudeList = new ArrayList<ProfissionalSaude>();
        for (var profissionalSaudeResponseDTO : profissionalSaudeResponseDTOList) {
            var profissionalSaude = toEntity(profissionalSaudeResponseDTO);
            profissionalSaudeList.add(profissionalSaude);
        }
        return profissionalSaudeList;
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

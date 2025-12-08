package com.alanpatrik.sghss.api.model;

import com.alanpatrik.sghss.api.model.dto.ProfissionalSaudeDTO;
import com.alanpatrik.sghss.api.model.dto.response.ProfissionalSaudeResponseDTO;
import com.alanpatrik.sghss.api.model.enums.AreaAtuacao;
import com.alanpatrik.sghss.api.model.enums.Especialidade;
import com.alanpatrik.sghss.api.security.crypto.Crypto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

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
    private Set<Consulta> consultas = new LinkedHashSet<>();

    @OneToOne(mappedBy = "profissionalSaude", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Agenda agenda;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "PROFISSIONAL_UNIDADE_SAUDE",
            joinColumns = @JoinColumn(name = "ID_PROFISSIONAL_SAUDE", referencedColumnName = "ID_PROFISSIONAL_SAUDE"),
            inverseJoinColumns = @JoinColumn(name = "ID_UNIDADE_SAUDE", referencedColumnName = "ID_UNIDADE_SAUDE")
    )
    @JsonIgnore
    private Set<UnidadeSaude> unidades = new LinkedHashSet<>();


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
            Set<Consulta> consultas,
            String CRM,
            Agenda agenda,
            Set<UnidadeSaude> unidades) {
        super(nome, cpf, dataNascimento, telefone, email, endereco, dataCriacao, dataModificacao);
        this.especialidade = especialidade;
        this.areaAtuacao = areaAtuacao;
        this.consultas = consultas;
        this.CRM = CRM;
        this.agenda = agenda;
        this.unidades = unidades;
    }

    public static ProfissionalSaudeResponseDTO toResponseDTO(ProfissionalSaude profissionalSaude) {
        var profissionalSaudeResponseDTO = new ProfissionalSaudeResponseDTO();
        profissionalSaudeResponseDTO.setId(profissionalSaude.getId());
        profissionalSaudeResponseDTO.setNome(profissionalSaude.getNome());
        profissionalSaudeResponseDTO.setCRM(profissionalSaude.getCRM());

        return profissionalSaudeResponseDTO;
    }

    public static ProfissionalSaude toEntity(ProfissionalSaudeResponseDTO profissionalSaudeResponseDTO) {
        var profissionalSaude = new ProfissionalSaude();
        profissionalSaude.setId(profissionalSaudeResponseDTO.getId());
        profissionalSaude.setNome(profissionalSaudeResponseDTO.getNome());
        profissionalSaude.setCRM(profissionalSaudeResponseDTO.getCRM());

        return profissionalSaude;
    }

    public static Set<ProfissionalSaudeResponseDTO> toResponseDTOList(Set<ProfissionalSaude> profissionaisSaude) {
        var profissionaisSaudeResponseDTO = new LinkedHashSet<ProfissionalSaudeResponseDTO>();
        for (var profissionalSaude : profissionaisSaude) {
            profissionaisSaudeResponseDTO.add(toResponseDTO(profissionalSaude));
        }
        return profissionaisSaudeResponseDTO;
    }

    public static Set<ProfissionalSaude> toEntityList(Set<ProfissionalSaudeResponseDTO> profissionaisSaudeDTO) {
        var profissionaisSaude = new LinkedHashSet<ProfissionalSaude>();
        for (var profissionalSaudeDTO : profissionaisSaudeDTO) {
            profissionaisSaude.add(ProfissionalSaude.toEntity(profissionalSaudeDTO));
        }
        return profissionaisSaude;
    }

    public static ProfissionalSaudeDTO toDTO(ProfissionalSaude profissionalSaude) {
        var profissionalSaudeDTO = new ProfissionalSaudeDTO();
        profissionalSaudeDTO.setId(profissionalSaude.getId());
        profissionalSaudeDTO.setNome(profissionalSaude.getNome());
        profissionalSaudeDTO.setCpf(Crypto.mascararCpf(profissionalSaude.getCpf()));
        profissionalSaudeDTO.setDataNascimento(profissionalSaude.getDataNascimento());
        profissionalSaudeDTO.setTelefone(profissionalSaude.getTelefone());
        profissionalSaudeDTO.setEmail(Crypto.mascararEmail(profissionalSaude.getEmail()));
        profissionalSaudeDTO.setEndereco(profissionalSaude.getEndereco());
        profissionalSaudeDTO.setDataCriacao(profissionalSaude.getDataCriacao());
        profissionalSaudeDTO.setDataModificacao(profissionalSaude.getDataModificacao());
        profissionalSaudeDTO.setEspecialidade(profissionalSaude.getEspecialidade());
        profissionalSaudeDTO.setAreaAtuacao(profissionalSaude.getAreaAtuacao());
        profissionalSaudeDTO.setConsultas(Consulta.toDTOList(profissionalSaude.getConsultas()));
        profissionalSaudeDTO.setCRM(profissionalSaude.getCRM());

        return profissionalSaudeDTO;
    }

    public static ProfissionalSaude toEntityResponse(ProfissionalSaudeDTO profissionalSaudeDTO) {
        var profissionalSaude = new ProfissionalSaude();
        profissionalSaude.setId(profissionalSaudeDTO.getId());
        profissionalSaude.setNome(profissionalSaudeDTO.getNome());
        profissionalSaude.setCpf(Crypto.mascararCpf(profissionalSaudeDTO.getCpf()));
        profissionalSaude.setDataNascimento(profissionalSaudeDTO.getDataNascimento());
        profissionalSaude.setTelefone(profissionalSaudeDTO.getTelefone());
        profissionalSaude.setEmail(Crypto.mascararEmail(profissionalSaudeDTO.getEmail()));
        profissionalSaude.setEndereco(profissionalSaudeDTO.getEndereco());
        profissionalSaude.setDataCriacao(profissionalSaudeDTO.getDataCriacao());
        profissionalSaude.setDataModificacao(profissionalSaudeDTO.getDataModificacao());
        profissionalSaude.setEspecialidade(profissionalSaudeDTO.getEspecialidade());
        profissionalSaude.setAreaAtuacao(profissionalSaudeDTO.getAreaAtuacao());
        profissionalSaude.setConsultas(profissionalSaude.getConsultas());
        profissionalSaude.setCRM(profissionalSaudeDTO.getCRM());

        return profissionalSaude;
    }
}

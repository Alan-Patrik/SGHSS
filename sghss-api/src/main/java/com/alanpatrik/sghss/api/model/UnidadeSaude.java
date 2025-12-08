package com.alanpatrik.sghss.api.model;

import com.alanpatrik.sghss.api.model.dto.UnidadeSaudeDTO;
import com.alanpatrik.sghss.api.model.dto.response.UnidadeSaudeResponseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "UNIDADE_SAUDE")
public class UnidadeSaude {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_UNIDADE_SAUDE", nullable = false)
    private Long id;

    @Column(name = "TXT_NOME", nullable = false)
    private String nome;

    @Embedded
    private Endereco endereco;

    @JsonIgnore
    @OneToMany(mappedBy = "unidadeSaude")
    private Set<Leito> leitos = new LinkedHashSet<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "unidades")
    private Set<ProfissionalSaude> profissionais = new LinkedHashSet<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "unidades")
    private Set<Paciente> pacientes = new LinkedHashSet<>();

    public static UnidadeSaudeResponseDTO toResponseDTO(UnidadeSaude unidadeSaude) {
        var unidadeSaudeResponseDTO = new UnidadeSaudeResponseDTO();
        unidadeSaudeResponseDTO.setId(unidadeSaude.getId());
        unidadeSaudeResponseDTO.setNome(unidadeSaude.getNome());

        return unidadeSaudeResponseDTO;
    }

    public static UnidadeSaude toEntity(UnidadeSaudeResponseDTO unidadeSaudeResponseDTO) {
        var unidadeSaude = new UnidadeSaude();
        unidadeSaude.setId(unidadeSaudeResponseDTO.getId());
        unidadeSaude.setNome(unidadeSaudeResponseDTO.getNome());

        return unidadeSaude;
    }

    public static UnidadeSaudeDTO toDTO(UnidadeSaude unidadeSaude) {
        var unidadeSaudeDTO = new UnidadeSaudeDTO();
        unidadeSaudeDTO.setId(unidadeSaude.getId());
        unidadeSaudeDTO.setNome(unidadeSaude.getNome());
        unidadeSaudeDTO.setEndereco(unidadeSaude.getEndereco());
        unidadeSaudeDTO.setProfissionais(ProfissionalSaude.toResponseDTOList(unidadeSaude.getProfissionais()));
        unidadeSaudeDTO.setLeitos(Leito.toResponseDTOList(unidadeSaude.getLeitos()));
        unidadeSaudeDTO.setPacientes(Paciente.toResponseDTOList(unidadeSaude.getPacientes()));

        return unidadeSaudeDTO;
    }

    public static UnidadeSaude toEntityResponse(UnidadeSaudeDTO unidadeSaudeDTO) {
        var unidadeSaude = new UnidadeSaude();
        unidadeSaude.setId(unidadeSaudeDTO.getId());
        unidadeSaude.setNome(unidadeSaudeDTO.getNome());
        unidadeSaude.setEndereco(unidadeSaudeDTO.getEndereco());
        unidadeSaude.setProfissionais(ProfissionalSaude.toEntityList(unidadeSaudeDTO.getProfissionais()));
        unidadeSaude.setLeitos(Leito.toEntityList(unidadeSaudeDTO.getLeitos()));

        return unidadeSaude;
    }
}

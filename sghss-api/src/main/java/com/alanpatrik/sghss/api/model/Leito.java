package com.alanpatrik.sghss.api.model;

import com.alanpatrik.sghss.api.model.dto.LeitoDTO;
import com.alanpatrik.sghss.api.model.dto.response.LeitoResponseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "LEITO")
public class Leito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_LEITO", nullable = false)
    private Long id;

    @Column(name = "TXT_NUMERO", nullable = false)
    private String numero;

    @OneToMany
    @Column(name = "ID_PACIENTE", nullable = false)
    private Set<Paciente> pacientes = new HashSet<>();

    @OneToMany
    @Column(name = "ID_PROFISSIONAL_SAUDE", nullable = false)
    private Set<ProfissionalSaude> profissionaisSaude = new HashSet<>();

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "ID_UNIDADE_SAUDE", nullable = false)
    private UnidadeSaude unidadeSaude;

    public static LeitoResponseDTO toResponseDTO(Leito leito) {
        var leitoResponseDTO = new LeitoResponseDTO();
        leitoResponseDTO.setId(leito.getId());
        leitoResponseDTO.setNumero(leito.getNumero());
        return leitoResponseDTO;
    }

    public static Leito toEntity(LeitoResponseDTO leitoResponseDTO) {
        var leito = new Leito();
        leito.setId(leitoResponseDTO.getId());
        leito.setNumero(leitoResponseDTO.getNumero());
        return leito;
    }

    public static LeitoDTO toDTO(Leito leito) {
        var leitoDTO = new LeitoDTO();
        leitoDTO.setId(leito.getId());
        leitoDTO.setNumero(leito.getNumero());
        leitoDTO.setUnidadeSaude(UnidadeSaude.toResponseDTO(leito.getUnidadeSaude()));
        leitoDTO.setPacientes(Paciente.toResponseDTOList(leito.getPacientes()));
        leitoDTO.setProfissionaisSaude(ProfissionalSaude.toResponseDTOList(leito.getProfissionaisSaude()));
        return leitoDTO;
    }

    public static Leito toEntityResponse(LeitoDTO leitoDTO) {
        var leito = new Leito();
        leito.setId(leitoDTO.getId());
        leito.setNumero(leitoDTO.getNumero());
        leito.setUnidadeSaude(UnidadeSaude.toEntity(leitoDTO.getUnidadeSaude()));
        leito.setPacientes(Paciente.toEntityList(leitoDTO.getPacientes()));
        leito.setProfissionaisSaude(ProfissionalSaude.toEntityList(leitoDTO.getProfissionaisSaude()));
        return leito;
    }

    public static Set<LeitoResponseDTO> toResponseDTOList(Set<Leito> leitos) {
        var leitoDTOs = new LinkedHashSet<LeitoResponseDTO>();
        for (var leito : leitos) {
            leitoDTOs.add(toResponseDTO(leito));
        }
        return leitoDTOs;
    }

    public static Set<Leito> toEntityList(Set<LeitoResponseDTO> leitoResponseDTOs) {
        var leitos = new LinkedHashSet<Leito>();
        for (var leitoResponseDTO : leitoResponseDTOs) {
            leitos.add(toEntity(leitoResponseDTO));
        }
        return leitos;
    }
}

package com.alanpatrik.sghss.api.model;

import com.alanpatrik.sghss.api.model.dto.response.UnidadeSaudeResponseDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "UNIDADE_SERVICO")
public class UnidadeSaude {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_INIDADE_SERVICO", nullable = false)
    private Long id;

    @Column(name = "TXT_NOME", nullable = false)
    private String nome;

    @Embedded
    private Endereco endereco;

    @OneToMany(mappedBy = "unidadeSaude")
    private List<Leito> leitos;

    @OneToMany(mappedBy = "unidadeSaude")
    private List<ProfissionalSaude> profissionais;

    public static UnidadeSaudeResponseDTO toResponseDTO(UnidadeSaude unidadeSaude) {
        var unidadeSaudeResponseDTO = new UnidadeSaudeResponseDTO();
        unidadeSaudeResponseDTO.setId(unidadeSaude.getId());
        unidadeSaudeResponseDTO.setNome(unidadeSaude.getNome());
        unidadeSaudeResponseDTO.setEndereco(unidadeSaude.getEndereco());
        unidadeSaudeResponseDTO.setProfissionais(unidadeSaude.getProfissionais());
        unidadeSaudeResponseDTO.setLeitos(unidadeSaude.getLeitos());

        return unidadeSaudeResponseDTO;
    }

    public static UnidadeSaude toEntityDTO(UnidadeSaudeResponseDTO unidadeSaudeResponseDTO) {
        var unidadeSaude = new UnidadeSaude();
        unidadeSaude.setId(unidadeSaudeResponseDTO.getId());
        unidadeSaude.setNome(unidadeSaudeResponseDTO.getNome());
        unidadeSaude.setEndereco(unidadeSaudeResponseDTO.getEndereco());
        unidadeSaude.setProfissionais(unidadeSaudeResponseDTO.getProfissionais());
        unidadeSaude.setLeitos(unidadeSaudeResponseDTO.getLeitos());

        return unidadeSaude;
    }

    public static List<UnidadeSaude> toResponseEntityList(List<UnidadeSaudeResponseDTO> unidadeSaudeResponseDTOList) {
        var unidadeSaudeResponseList = new ArrayList<UnidadeSaude>();
        for (var unidadeSaude : unidadeSaudeResponseDTOList) {
            var unidadeSaudeDTO = toEntityDTO(unidadeSaude);
            unidadeSaudeResponseList.add(unidadeSaudeDTO);
        }

        return unidadeSaudeResponseList;
    }

    public static List<UnidadeSaudeResponseDTO> toResponseDTOList(List<UnidadeSaude> unidadeSaudeList) {
        var unidadeSaudeResponseDTOList = new ArrayList<UnidadeSaudeResponseDTO>();
        for (var unidadeSaude : unidadeSaudeList) {
            var unidadeSaudeResponseDTO = toResponseDTO(unidadeSaude);
            unidadeSaudeResponseDTOList.add(unidadeSaudeResponseDTO);
        }

        return unidadeSaudeResponseDTOList;
    }
}

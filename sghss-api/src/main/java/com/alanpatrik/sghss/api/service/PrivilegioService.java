package com.alanpatrik.sghss.api.service;

import com.alanpatrik.sghss.api.exception.ConflitoException;
import com.alanpatrik.sghss.api.exception.InformacaoNaoEncontradaException;
import com.alanpatrik.sghss.api.exception.ParametroInvalidoException;
import com.alanpatrik.sghss.api.model.Privilegio;
import com.alanpatrik.sghss.api.model.dto.request.PrivilegioRequestDTO;
import com.alanpatrik.sghss.api.model.dto.response.PrivilegioResponseDTO;
import com.alanpatrik.sghss.api.repository.PrivilegioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class PrivilegioService {

    private final PrivilegioRepository privilegioRepository;

    @Transactional(readOnly = true)
    public List<PrivilegioResponseDTO> findAll() {
        return privilegioRepository.findAll().stream()
                .map(Privilegio::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public PrivilegioResponseDTO findByName(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new ParametroInvalidoException("O campo Nome é obrigatório.");
        }

        var privilegio = privilegioRepository.findByName(nome).orElseThrow(() ->
                new InformacaoNaoEncontradaException("Privilégio não encontrado: " + nome));
        return Privilegio.toResponseDTO(privilegio);
    }

    public PrivilegioResponseDTO createPrivilege(PrivilegioRequestDTO privilegioRequestDTO) {
        privilegioRepository.findByName(privilegioRequestDTO.getNome()).ifPresent(p -> {
            throw new ConflitoException("Privilegio já existe: " + privilegioRequestDTO.getNome());
        });

        var privilegio = Privilegio.builder().name(privilegioRequestDTO.getNome()).build();
        privilegio = privilegioRepository.save(privilegio);
        return Privilegio.toResponseDTO(privilegio);
    }
}

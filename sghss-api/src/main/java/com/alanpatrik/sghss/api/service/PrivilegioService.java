package com.alanpatrik.sghss.api.service;

import com.alanpatrik.sghss.api.comum.Constantes;
import com.alanpatrik.sghss.api.exception.ConflitoException;
import com.alanpatrik.sghss.api.exception.InformacaoNaoEncontradaException;
import com.alanpatrik.sghss.api.exception.ParametroInvalidoException;
import com.alanpatrik.sghss.api.model.Privilegio;
import com.alanpatrik.sghss.api.model.dto.request.PrivilegioRequestDTO;
import com.alanpatrik.sghss.api.model.dto.response.PrivilegioResponseDTO;
import com.alanpatrik.sghss.api.repository.PrivilegioRepository;
import com.alanpatrik.sghss.api.security.anotation.RequireRoles;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class PrivilegioService {

    private final PrivilegioRepository privilegioRepository;

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional(readOnly = true)
    public List<PrivilegioResponseDTO> findAll() {
        return privilegioRepository.findAll().stream()
                .map(Privilegio::toResponseDTO)
                .toList();
    }

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional(readOnly = true)
    public PrivilegioResponseDTO findByName(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new ParametroInvalidoException("O campo Nome é obrigatório.");
        }

        var privilegio = privilegioRepository.findByName(nome).orElseThrow(() ->
                new InformacaoNaoEncontradaException(
                        Constantes.PRIVILEGIO_NOT_FOUND_BY_NAME_MESSAGE.replace("%s", nome)
                ));
        return Privilegio.toResponseDTO(privilegio);
    }

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional(readOnly = true)
    public PrivilegioResponseDTO findById(Long id) {
        var privilegio = privilegioRepository.findById(id).orElseThrow(() ->
                new InformacaoNaoEncontradaException(
                        Constantes.PRIVILEGIO_NOT_FOUND_BY_ID_MESSAGE.replace("%s", String.valueOf(id))
                ));
        return Privilegio.toResponseDTO(privilegio);
    }

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PrivilegioResponseDTO createPrivilege(PrivilegioRequestDTO privilegioRequestDTO) {
        privilegioRepository.findByName(privilegioRequestDTO.getNome()).ifPresent(p -> {
            throw new ConflitoException(
                    Constantes.PRIVILEGIO_CONFLICT_BY_NAME_MESSAGE
                            .replace("%s", privilegioRequestDTO.getNome()));
        });

        var privilegio = Privilegio.builder()
                .name(privilegioRequestDTO.getNome())
                .build();

        privilegio = privilegioRepository.save(privilegio);
        return Privilegio.toResponseDTO(privilegio);
    }

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional
    public String delete(Long id) {
        var privilegio = privilegioRepository.findById(id).orElseThrow(() ->
                new InformacaoNaoEncontradaException(
                        Constantes.PRIVILEGIO_NOT_FOUND_BY_ID_MESSAGE.replace("%s", String.valueOf(id))
                ));

        var joins = new HashSet<>(privilegio.getRolePrivilegios());
        for (var rolePrivilegio : joins) {
            var role = rolePrivilegio.getRole();
            if (role != null) {
                role.getRolePrivilegios().remove(rolePrivilegio);
            }
            privilegio.getRolePrivilegios().remove(rolePrivilegio);
        }
        privilegioRepository.delete(privilegio);

        return "Privilégio deletado com sucesso.";
    }
}

package com.alanpatrik.sghss.api.service;

import com.alanpatrik.sghss.api.exception.ConflitoException;
import com.alanpatrik.sghss.api.exception.InformacaoNaoEncontradaException;
import com.alanpatrik.sghss.api.exception.ParametroInvalidoException;
import com.alanpatrik.sghss.api.model.Role;
import com.alanpatrik.sghss.api.model.RolePrivilegio;
import com.alanpatrik.sghss.api.model.dto.request.RoleRequestDTO;
import com.alanpatrik.sghss.api.model.dto.response.RoleResponseDTO;
import com.alanpatrik.sghss.api.repository.PrivilegioRepository;
import com.alanpatrik.sghss.api.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final PrivilegioRepository privilegioRepository;

    @Transactional(readOnly = true)
    public RoleResponseDTO findByAuthority(String authority) {
        var roleDTO = roleRepository.findByAuthority(authority).orElseThrow(() ->
                new InformacaoNaoEncontradaException("Role não encontrado."));

        return Role.toResponseDTO(roleDTO);
    }

    public RoleResponseDTO createRole(RoleRequestDTO roleRequestDTO) {
        this.validarParametrosObrigatorios(roleRequestDTO);

        roleRepository.findByAuthority(roleRequestDTO.getAuthority()).ifPresent(r -> {
            throw new ConflitoException("Role já existe: " + roleRequestDTO.getAuthority());
        });

        var role = Role.builder()
                .authority(roleRequestDTO.getAuthority())
                .rolePrivilegios(roleRequestDTO.getRolePrivilegios())
                .build();

        role = roleRepository.save(role);
        return Role.toResponseDTO(role);
    }

    public void addPrivilegioToRole(Long roleId, Long privilegeId) {
        this.validarParametrosObrigatorios(roleId, privilegeId);

        var role = roleRepository.findById(roleId).orElseThrow(() ->
                new InformacaoNaoEncontradaException("Role não encontrado."));

        var privilegio = privilegioRepository.findById(privilegeId).orElseThrow(() ->
                new InformacaoNaoEncontradaException("Privilege não encontrado."));

        var rolePrivilegio = RolePrivilegio.builder()
                .role(role)
                .privilegio(privilegio)
                .build();

        role.getRolePrivilegios().add(rolePrivilegio);
        roleRepository.save(role);
    }

    public void removePrivilegeFromRole(Long roleId, Long privilegeId) {
        this.validarParametrosObrigatorios(roleId, privilegeId);

        var role = roleRepository.findById(roleId).orElseThrow(() ->
                new InformacaoNaoEncontradaException("Role não encontrado."));

        role.getRolePrivilegios().removeIf(rp -> rp.getPrivilegio().getId().equals(privilegeId));
        roleRepository.save(role);
    }

    private void validarParametrosObrigatorios(RoleRequestDTO roleRequestDTO) {
        if (roleRequestDTO.getAuthority() == null || roleRequestDTO.getAuthority().isBlank()) {
            throw new ParametroInvalidoException("O campo Authority é obrigatório.");
        }
        if (roleRequestDTO.getRolePrivilegios() == null || roleRequestDTO.getRolePrivilegios().isEmpty()) {
            throw new ParametroInvalidoException("O campo Authority é obrigatório.");
        }
    }

    private void validarParametrosObrigatorios(Long roleId, Long privilegeId) {
        if (roleId == null || roleId == 0) {
            throw new ParametroInvalidoException("O campo Role_ID é obrigatório.");
        }
        if (privilegeId == null || privilegeId == 0) {
            throw new ParametroInvalidoException("O campo Role_ID é obrigatório.");
        }
    }
}


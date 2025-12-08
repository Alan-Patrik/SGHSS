package com.alanpatrik.sghss.api.service;

import com.alanpatrik.sghss.api.comum.Constantes;
import com.alanpatrik.sghss.api.exception.ConflitoException;
import com.alanpatrik.sghss.api.exception.InformacaoNaoEncontradaException;
import com.alanpatrik.sghss.api.exception.ParametroInvalidoException;
import com.alanpatrik.sghss.api.model.Role;
import com.alanpatrik.sghss.api.model.RolePrivilegio;
import com.alanpatrik.sghss.api.model.dto.request.RoleRequestDTO;
import com.alanpatrik.sghss.api.model.dto.response.RoleResponseDTO;
import com.alanpatrik.sghss.api.repository.PrivilegioRepository;
import com.alanpatrik.sghss.api.repository.RoleRepository;
import com.alanpatrik.sghss.api.security.anotation.RequireRoles;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@RequiredArgsConstructor
@Transactional
@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final PrivilegioRepository privilegioRepository;

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public RoleResponseDTO createRole(RoleRequestDTO roleRequestDTO) {
        this.validarParametrosObrigatorios(roleRequestDTO);

        roleRepository.findByAuthority(roleRequestDTO.getAuthority()).ifPresent(r -> {
            throw new ConflitoException(
                    Constantes.ROLE_CONFLICT_MESSAGE.replace("%s", roleRequestDTO.getAuthority()));
        });

        var role = Role.builder()
                .authority(roleRequestDTO.getAuthority())
                .rolePrivilegios(new HashSet<>())
                .usuarioRoles(new HashSet<>())
                .build();

        role = roleRepository.save(role);
        return Role.toResponseDTO(role);
    }

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional
    public String addPrivilegioToRole(long roleId, long privilegeId) {
        this.validarParametrosObrigatorios(roleId, privilegeId);

        var role = roleRepository.findById(roleId).orElseThrow(() ->
                new InformacaoNaoEncontradaException(
                        Constantes.ROLE_NOT_FOUND_BY_ID_MESSAGE.replace("%s", String.valueOf(roleId))
                ));

        var privilegio = privilegioRepository.findById(privilegeId).orElseThrow(() ->
                new InformacaoNaoEncontradaException(
                        Constantes.PRIVILEGIO_NOT_FOUND_BY_ID_MESSAGE.replace("%s", String.valueOf(privilegeId))
                ));

        var rolePrivilegio = RolePrivilegio.builder()
                .role(role)
                .privilegio(privilegio)
                .build();

        role.getRolePrivilegios().add(rolePrivilegio);
        roleRepository.save(role);
        return "Privilégio adicionado com sucesso!";
    }

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional
    public String removePrivilegeFromRole(Long roleId, Long privilegeId) {
        this.validarParametrosObrigatorios(roleId, privilegeId);

        var role = roleRepository.findById(roleId).orElseThrow(() ->
                new InformacaoNaoEncontradaException(
                        Constantes.ROLE_NOT_FOUND_BY_ID_MESSAGE.replace("%s", String.valueOf(roleId))
                ));

        privilegioRepository.findById(privilegeId).orElseThrow(() ->
                new InformacaoNaoEncontradaException(
                        Constantes.PRIVILEGIO_NOT_FOUND_BY_ID_MESSAGE.replace("%s", String.valueOf(privilegeId))
                ));

        role.getRolePrivilegios().removeIf(rolePrivilegio ->
                rolePrivilegio.getPrivilegio().getId().equals(privilegeId));

        roleRepository.save(role);
        return "Privilegio removido com sucesso.";
    }

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional
    public String delete(Long id) {
        var role = roleRepository.findById(id).orElseThrow(() ->
                new InformacaoNaoEncontradaException(
                        Constantes.ROLE_NOT_FOUND_BY_ID_MESSAGE.replace("%s", String.valueOf(id))
                ));

        var rolePrivilegios = new HashSet<>(role.getRolePrivilegios());
        for (RolePrivilegio rolePrivilegio : rolePrivilegios) {
            var privilegio = rolePrivilegio.getPrivilegio();
            if (privilegio != null) {
                privilegio.getRolePrivilegios().remove(rolePrivilegio);
            }
            role.getRolePrivilegios().remove(rolePrivilegio);
        }

        var urs = new HashSet<>(role.getUsuarioRoles());
        for (var usuarioRole : urs) {
            var usuario = usuarioRole.getUsuario();
            if (usuario != null) {
                usuario.getUsuarioRoles().remove(usuarioRole);
            }
        }

        roleRepository.delete(role);
        return "Role deletada com sucesso.";

    }

    private void validarParametrosObrigatorios(RoleRequestDTO roleRequestDTO) {
        if (roleRequestDTO.getAuthority() == null || roleRequestDTO.getAuthority().isBlank()) {
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


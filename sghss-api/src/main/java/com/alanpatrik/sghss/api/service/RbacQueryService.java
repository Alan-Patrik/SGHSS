package com.alanpatrik.sghss.api.service;


import com.alanpatrik.sghss.api.comum.Constantes;
import com.alanpatrik.sghss.api.exception.InformacaoNaoEncontradaException;
import com.alanpatrik.sghss.api.model.dto.response.PrivilegioResponseDTO;
import com.alanpatrik.sghss.api.model.dto.response.RoleResponseDTO;
import com.alanpatrik.sghss.api.repository.UsuarioRepository;
import com.alanpatrik.sghss.api.security.anotation.RequireRoles;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static java.util.stream.Collectors.toCollection;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RbacQueryService {

    private final UsuarioRepository usuarioRepository;

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional(readOnly = true)
    public Set<PrivilegioResponseDTO> getPrivilegesOfUser(String username) {
        var usuario = usuarioRepository.loadUserGraph(username).orElseThrow(() ->
                new InformacaoNaoEncontradaException(
                        Constantes.USUARIO_NOT_FOUND_BY_USERNAME_MESSAGE.replace("%s", username)
                ));

        return usuario.getUsuarioRoles().stream()
                .flatMap(usuarioRole -> usuarioRole.getRole().getRolePrivilegios().stream())
                .map(rolePrivilegio -> new PrivilegioResponseDTO(
                        rolePrivilegio.getPrivilegio().getId(),
                        rolePrivilegio.getPrivilegio().getName()
                ))
                .collect(toCollection(HashSet::new));
    }

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional(readOnly = true)
    public Set<RoleResponseDTO> getRolesOfUser(String username) {
        var usuario = usuarioRepository.loadUserGraph(username).orElseThrow(() ->
                new InformacaoNaoEncontradaException(
                        Constantes.USUARIO_NOT_FOUND_BY_USERNAME_MESSAGE.replace("%s", username)));

        Set<RoleResponseDTO> roleResponseDTOS = new LinkedHashSet<>();
        for (var usuarioRole : usuario.getUsuarioRoles()) {
            if (usuarioRole == null || usuarioRole.getRole() == null) {
                continue;
            }

            var role = usuarioRole.getRole();
            Set<String> privilegios = new LinkedHashSet<>();
            if (role.getRolePrivilegios() != null) {
                for (var rolePrivilegio : role.getRolePrivilegios()) {
                    if (rolePrivilegio == null || rolePrivilegio.getPrivilegio() == null) {
                        continue;
                    }
                    var nome = rolePrivilegio.getPrivilegio().getName();
                    if (nome != null) {
                        privilegios.add(nome);
                    }
                }
            }

            var roleResponseDTO = RoleResponseDTO.builder()
                    .id(role.getId())
                    .authority(role.getAuthority())
                    .rolePrivilegios(privilegios)
                    .build();

            roleResponseDTOS.add(roleResponseDTO);
        }

        return roleResponseDTOS;

    }
}


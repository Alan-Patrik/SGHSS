package com.alanpatrik.sghss.api.util;

import com.alanpatrik.sghss.api.model.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class LogonUtils {

    private LogonUtils() {
    }

    /**
     * Extrai authorities (Strings) de roles e privilégios do grafo do usuário.
     */
    public static Set<String> extractAuthorityStrings(Usuario usuario) {
        if (usuario == null || usuario.getUsuarioRoles() == null) {
            return Collections.emptySet();
        }

        Set<String> authorities = new HashSet<>();
        for (var usuarioRole : usuario.getUsuarioRoles()) {
            if (usuarioRole == null) continue;
            var role = usuarioRole.getRole();
            if (role != null && StringUtils.hasText(role.getAuthority())) {
                authorities.add(role.getAuthority());
            }
            if (role != null && role.getRolePrivilegios() != null) {
                for (var rolePrivilege : role.getRolePrivilegios()) {
                    var privilegio = rolePrivilege.getPrivilegio();
                    if (privilegio != null && StringUtils.hasText(privilegio.getName())) {
                        authorities.add(privilegio.getName());
                    }
                }
            }
        }
        return authorities;
    }

    /**
     * Converte Strings de autoridade em GrantedAuthority.
     */
    public static List<GrantedAuthority> toGrantedAuthorities(Set<String> authorityStrings) {
        if (authorityStrings == null || authorityStrings.isEmpty()) {
            return List.of();
        }
        return authorityStrings.stream()
                .filter(StringUtils::hasText)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    /**
     * Verifica se o Authentication possui uma autoridade específica.
     */
    public static boolean hasAuthority(Authentication auth, String authority) {
        if (auth == null || !StringUtils.hasText(authority)) {
            return false;
        }
        return auth.getAuthorities().stream()
                .anyMatch(a -> authority.equalsIgnoreCase(a.getAuthority()));
    }
}

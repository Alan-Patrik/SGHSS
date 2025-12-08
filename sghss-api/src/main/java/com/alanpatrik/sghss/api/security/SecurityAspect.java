package com.alanpatrik.sghss.api.security;

import com.alanpatrik.sghss.api.exception.AcessoProibidoException;
import com.alanpatrik.sghss.api.security.anotation.RequireRoles;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Aspect
@Component
public class SecurityAspect {

    // Prefixos públicos (ajuste conforme necessário)
    private static final List<String> PUBLIC_PREFIXES = List.of(
            "/auth/login",
            "/h2-console",
            "/v3/api-docs",
            "/swagger-ui",
            "/swagger-resources",
            "/webjars"
    );

    @Around("@within(requireRoles) || @annotation(requireRoles)")
    public Object checkServiceRoles(ProceedingJoinPoint pjp, RequireRoles requireRoles) throws Throwable {
        String uri = resolveCurrentUri();

        // Se a URI for pública, não aplica verificação de roles
        if (isPublicUri(uri)) {
            return pjp.proceed();
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new AcessoProibidoException("Usuário não autenticado");
        }

        // Extrai roles normalizadas (sem prefixo ROLE_)
        Set<String> userRoles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(this::stripRolePrefix)
                .collect(Collectors.toSet());

        String[] required = requireRoles.value();
        boolean authorized = (requireRoles.mode() == RequireRoles.Mode.ALL)
                ? Arrays.stream(required).allMatch(userRoles::contains)
                : Arrays.stream(required).anyMatch(userRoles::contains);

        if (!authorized) {
            throw new AcessoProibidoException("Acesso negado! Você não possui as credenciais necessárias.");
        }

        return pjp.proceed();
    }

    private String stripRolePrefix(String authority) {
        return (authority != null && authority.startsWith("ROLE_"))
                ? authority.substring("ROLE_".length())
                : authority;
    }

    private String resolveCurrentUri() {
        var attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) return null;
        HttpServletRequest request = attrs.getRequest();
        return request != null ? request.getRequestURI() : null;
    }

    private boolean isPublicUri(String uri) {
        if (uri == null) return false;
        return PUBLIC_PREFIXES.stream().anyMatch(uri::startsWith);
    }
}

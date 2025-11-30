package com.alanpatrik.sghss.api.auditoria;

import com.alanpatrik.sghss.api.model.dto.request.AuditoriaRequestDTO;
import com.alanpatrik.sghss.api.service.AuditoriaService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Aspect
public class AuditoriaAspecto {
    private final AuditoriaService auditoriaService;
    private final HttpServletRequest httpServletRequest;

    @Around("@annotation(auditable)")
    public Object record(ProceedingJoinPoint proceedingJoinPoint, Auditable auditable) throws Throwable {
        var resultado = proceedingJoinPoint.proceed();
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var usuario = authentication != null ? authentication.getName() : "system";
        var ip = httpServletRequest.getRemoteAddr();
        var auditoriaRequestDTO = AuditoriaRequestDTO.builder()
                .usuario(usuario)
                .acao(auditable.action())
                .nomeEntidade(auditable.entity())
                .idEntidade("-")
                .ip(ip)
                .detalhes("method=" + proceedingJoinPoint.getSignature().toShortString())
                .build();
        auditoriaService.save(auditoriaRequestDTO);
        return resultado;
    }
}

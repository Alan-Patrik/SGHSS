package com.alanpatrik.sghss.api.auditoria;

import com.alanpatrik.sghss.api.model.dto.request.AuditoriaRequestDTO;
import com.alanpatrik.sghss.api.service.AuditoriaService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class AuditoriaLoggingFilter extends OncePerRequestFilter {

    private final AuditoriaService auditoriaService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        var start = System.currentTimeMillis();
        try {
            chain.doFilter(request, response);
        } finally {
            var ms = System.currentTimeMillis() - start;

            var auth = SecurityContextHolder.getContext().getAuthentication();
            var username = (auth != null) ? auth.getName() : "anonymous";
            var method = request.getMethod();
            var resource = request.getRequestURI();
            var ip = getClientIp(request);
            var ua = request.getHeader("User-Agent");
            var action = "HTTP_" + method;
            var details = String.format("{\"status\":%d,\"durationMs\":%d}", response.getStatus(), ms);

            var auditoriaRequestDTO = AuditoriaRequestDTO.builder()
                    .username(username)
                    .action(action)
                    .resource(resource)
                    .ip(ip)
                    .userAgent(StringUtils.hasText(ua) ? ua : null)
                    .details(details)
                    .httpMethod(method)
                    .build();

            auditoriaService.create(auditoriaRequestDTO);
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String xf = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(xf)) {
            // Pega o primeiro IP (cadeia de proxies)
            return xf.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}


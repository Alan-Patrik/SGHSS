package com.alanpatrik.sghss.api.config;

import com.alanpatrik.sghss.api.auditoria.AuditoriaLoggingFilter;
import com.alanpatrik.sghss.api.service.AuditoriaService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuditoriaConfig {

    @Bean
    public FilterRegistrationBean<AuditoriaLoggingFilter> auditFilterRegistration(AuditoriaService auditoriaService) {
        FilterRegistrationBean<AuditoriaLoggingFilter> reg = new FilterRegistrationBean<>();
        reg.setFilter(new AuditoriaLoggingFilter(auditoriaService));
        reg.addUrlPatterns("/*");           // intercepta todas as rotas
        reg.setOrder(100);                  // ordem de execução
        return reg;
    }
}

package com.alanpatrik.sghss.api.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusConsulta {
    N("N", "Não iniciada"),
    E("E", "Em andamento"),
    F("F", "Finalizada"),
    C("C", "Cancelada");

    private final String codigo;
    private final String descricao;

    public static StatusConsulta getEnum(String codigo) {
        if (codigo == null) {
            return null;
        }
        for (StatusConsulta e : StatusConsulta.values()) {
            if (e.getCodigo().equals(codigo)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Status da Consulta não encontrada para o código: ".concat(codigo));
    }
}

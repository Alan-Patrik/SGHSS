package com.alanpatrik.sghss.api.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusHorario {
    D("D", "Disponível"),
    N("N", "Não disponível");

    private final String codigo;
    private final String descricao;

    public static StatusHorario getEnum(String codigo) {
        if (codigo == null) {
            return null;
        }
        for (StatusHorario e : StatusHorario.values()) {
            if (e.getCodigo().equals(codigo)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Status da Consulta não encontrada para o código: ".concat(codigo));
    }
}

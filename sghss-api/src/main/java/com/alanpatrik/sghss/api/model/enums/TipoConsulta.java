package com.alanpatrik.sghss.api.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TipoConsulta {
    P("P", "Presencial"),
    O("O", "Online");

    private final String codigo;
    private final String descricao;

    public static TipoConsulta getEnum(String codigo) {
        if (codigo == null) {
            return null;
        }
        for (TipoConsulta e : TipoConsulta.values()) {
            if (e.getCodigo().equals(codigo)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Tipo da Consulta não encontrado para o código: ".concat(codigo));
    }
}

package com.alanpatrik.sghss.api.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Especialidade {
    E("E", "Enfermagem"),
    M("M", "Médico"),
    T("T", "Técnico");

    private final String codigo;
    private final String descricao;

    public static Especialidade getEnum(String codigo) {
        if (codigo == null) {
            return null;
        }
        for (Especialidade e : Especialidade.values()) {
            if (e.getCodigo().equals(codigo)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Especialidade não encontrada para o código: ".concat(codigo));
    }
}

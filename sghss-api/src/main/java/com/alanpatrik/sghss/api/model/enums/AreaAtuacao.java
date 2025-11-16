package com.alanpatrik.sghss.api.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AreaAtuacao {
    CD("CD", "Cardiologista"),
    CG("CG", "Clínico geral"),
    GI("GI", "Ginecologista"),
    NU("NU", "Nutricionista"),
    OF("OF", "Oftalmologista"),
    OR("OR", "Ortopedista"),
    OT("OT", "Otorrinolaringologista"),
    PE("PE", "Pediatra"),
    PR("PR", "Proctologista"),
    RA("RA", "Radiologista"),
    UR("UR", "Urologista");

    private final String codigo;
    private final String descricao;

    public static AreaAtuacao getEnum(String codigo) {
        if (codigo == null) {
            return null;
        }
        for (AreaAtuacao e : AreaAtuacao.values()) {
            if (e.getCodigo().equals(codigo)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Área de atuação não encontrada para o código: ".concat(codigo));
    }
}

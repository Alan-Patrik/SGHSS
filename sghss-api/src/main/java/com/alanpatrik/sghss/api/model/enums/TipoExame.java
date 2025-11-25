package com.alanpatrik.sghss.api.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TipoExame {
    // Cardiologista
    ECG("ECG", "Eletrocardiograma (ECG)"),
    ECO("ECO", "Ecocardiograma"),
    TE("TE", "Teste ergométrico (teste de esforço)"),
    HO("HO", "Holter 24h"),
    MAPA("MAPA", "Monitoramento ambulatorial da pressão arterial (MAPA)"),
    AC("AC", "Angiotomografia coronária"),

    // Clínico Geral
    HC("HC", "Hemograma completo"),
    GJ("GJ", "Glicemia em jejum"),
    FR("FR", "Função renal (ureia e creatinina)"),
    FH("FH", "Função hepática (TGO, TGP)"),

    // Ginecologista
    PA("PA", "Papanicolau (citologia oncótica)"),
    UT("UT", "Ultrassom transvaginal"),
    CPA("CPA", "Colposcopia"),
    UPE("UPE", "Ultrassom pélvico"),
    DH("DH", "Dosagem hormonal (estradiol, progesterona)"),
    CIV("EAS", "Cultura para infecções vaginais"),

    // Nutricionista
    ACC("ACC", "Avaliação de composição corporal (bioimpedância)"),
    EV("EV", "Exame de vitaminas"),
    PL("PL", "Perfil lipídico"),
    HG("HG", "Hemoglobina glicada"),
    EFF("EFF", "Exame de ferro e ferritina"),
    ACM("ACM", "Avaliação de cálcio e magnésio"),

    // Oftalmologista
    ECV("ECV", "Exame de acuidade visual"),
    TO("TO", "Tonometria (pressão intraocular)"),
    MR("MR", "Mapeamento de retina"),
    CA("CA", "Campimetria (campo visual)"),
    TCO("TCO", "Topografia corneana"),
    RE("RE", "Retinografia"),

    // Ortopedista
    RA("RA", "Radiografia (Raio-X)"),
    DO("DO", "Densitometria óssea"),
    UM("UM", "Ultrassom musculoesquelético"),
    AD("AD", "Artroscopia diagnóstica"),

    // Otorrinolaringologista
    AU("AU", "Audiometria"),
    NA("NA", "Nasofibroscopia"),
    EO("EO", "Exame de otoscopia"),
    IM("IM", "Impedanciometria"),
    VI("VI", "Videolaringoscopia"),
    TS("TS", "Teste vestibular"),

    // Pediatra
    EFG("EFG", "Exame físico geral"),
    EAS("EAS", "Exame de urina"),
    EPS("EPS", "Exame parasitológico de fezes"),
    DV("DV", "Dosagem de vitamina D"),
    TP("TP", "Teste do pezinho"),

    // Proctologista
    COL("COL", "Colonoscopia"),
    RO("RO", "Retossigmoidoscopia"),
    ETR("ETR", "Exame de toque retal"),
    ANU("ANU", "Anuscopia"),
    PSF("PSF", "Pesquisa de sangue oculto nas fezes"),
    UE("UE", "Ultrassom endorretal"),

    // Radiologista
    TC("TC", "Tomografia computadorizada"),
    RM("RM", "Ressonância magnética"),
    UL("UL", "Ultrassonografia"),
    MA("MA", "Mamografia"),
    AN("AN", "Angiografia"),

    // Urologista
    PSA("PSA", "Antígeno Prostático Específico"),
    UP("UP", "Ultrassom de próstata"),
    URO("URO", "Urocultura"),
    URE("URE", "Uretrocistografia"),
    ECU("ECU", "Exame de creatinina e ureia");


    private final String codigo;
    private final String descricao;

    public static TipoExame getEnum(String codigo) {
        if (codigo == null) {
            return null;
        }
        for (TipoExame e : TipoExame.values()) {
            if (e.getCodigo().equals(codigo)) {
                return e;
            }
        }
        throw new IllegalArgumentException("TipoExame não encontrado para o código: ".concat(codigo));
    }
}

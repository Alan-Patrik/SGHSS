package com.alanpatrik.sghss.api.security.crypto;

public class Crypto {

    // Aplica máscara ***.UVW.XYZ-**
    public static String mascararCpf(String cpf) {
        if (cpf == null) return "***.***.***-**";

        var digitos = cpf.replaceAll("\\D", "");
        while (digitos.length() < 11) {
            digitos += "*";
        }

        var parteInicial = digitos.substring(3, 6);
        var parteMeio = digitos.substring(6, 9);
//        var parteFinal = digitos.substring(9, 11);

        return String.format("***.%s.%s-**", parteInicial, parteMeio);
    }

    // Aplica máscara "**@dominio.com"
    public static String mascararEmail(String email) {
        if (email == null || !email.contains("@")) {
            return "***";
        }

        String[] parts = email.split("@", 2);
        var local = parts[0];
        var domain = parts[1];

        if (local.isEmpty()) {
            return "**@" + domain;
        }

        var maskedLocal = "";
        int len = local.length();
        if (len <= 2) {
            maskedLocal = "**";
        } else {
            var prefix = local.substring(0, 2);
            int stars = Math.max(1, len - 2);
            maskedLocal = prefix + "*".repeat(stars);
        }

        return maskedLocal + "@" + domain;
    }
}


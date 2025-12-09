package com.alanpatrik.sghss.api.service;

import com.alanpatrik.sghss.api.comum.Constantes;
import com.alanpatrik.sghss.api.model.dto.ArquivoExportadoDTO;
import com.alanpatrik.sghss.api.repository.PagamentoRepository;
import com.alanpatrik.sghss.api.security.anotation.RequireRoles;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class RelatorioFinanceiroService {

    private final PagamentoRepository pagamentoRepository;

    @RequireRoles({Constantes.PRIV_GERAR_RELATORIOS})
    @Transactional
    public ArquivoExportadoDTO exportarFaturamentoMensalCsv(String inicio, String fim) {
        var dataInicio = parse(inicio);
        var dataFim = parse(fim);
        var dados = pagamentoRepository.faturamentoMensal(dataInicio, dataFim);

        var stringBuilder = new StringBuilder();
        stringBuilder.append("Ano;Mes;Total\n");
        for (var dado : dados) {
            stringBuilder.append(dado.getAno()).append(";")
                    .append(dado.getMes()).append(";")
                    .append(dado.getTotal()).append("\n");
        }
        return new ArquivoExportadoDTO(stringBuilder.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8),
                "faturamento_mensal.csv", "text/csv");
    }

    @RequireRoles({Constantes.PRIV_GERAR_RELATORIOS})
    @Transactional
    public ArquivoExportadoDTO exportarFaturamentoMensalXlsx(String inicio, String fim) {
        var dataInicio = parse(inicio);
        var dataFim = parse(fim);
        var dados = pagamentoRepository.faturamentoMensal(dataInicio, dataFim);

        try (var wbSheets = new org.apache.poi.xssf.usermodel.XSSFWorkbook()) {
            var sheet = wbSheets.createSheet("Faturamento Mensal");
            int r = 0;
            var header = sheet.createRow(r++);
            header.createCell(0).setCellValue("Ano");
            header.createCell(1).setCellValue("Mês");
            header.createCell(2).setCellValue("Total");

            for (var dado : dados) {
                var row = sheet.createRow(r++);
                row.createCell(0).setCellValue(dado.getAno());
                row.createCell(1).setCellValue(dado.getMes());
                row.createCell(2).setCellValue(dado.getTotal().doubleValue());
            }

            try (var baos = new ByteArrayOutputStream()) {
                wbSheets.write(baos);
                return new ArquivoExportadoDTO(
                        baos.toByteArray(),
                        "faturamento_mensal.xlsx",
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                );
            }
        } catch (IOException e) {
            throw new RuntimeException("Falha ao gerar XLSX do faturamento mensal", e);
        }
    }

    private LocalDateTime parse(String iso) {
        try {
            return LocalDateTime.parse(iso);
        } catch (Exception e) {
            throw new IllegalArgumentException("Data inválida. Use ISO-8601: yyyy-MM-dd'T'HH:mm:ss", e);
        }
    }
}


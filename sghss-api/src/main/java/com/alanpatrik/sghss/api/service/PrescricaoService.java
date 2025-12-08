package com.alanpatrik.sghss.api.service;

import com.alanpatrik.sghss.api.comum.Constantes;
import com.alanpatrik.sghss.api.exception.ConflitoException;
import com.alanpatrik.sghss.api.exception.InformacaoNaoEncontradaException;
import com.alanpatrik.sghss.api.exception.ParametroInvalidoException;
import com.alanpatrik.sghss.api.model.Prescricao;
import com.alanpatrik.sghss.api.model.Prontuario;
import com.alanpatrik.sghss.api.model.dto.request.PrescricaoRequestDTO;
import com.alanpatrik.sghss.api.model.dto.response.PrescricaoResponseDTO;
import com.alanpatrik.sghss.api.repository.PrescricaoRepository;
import com.alanpatrik.sghss.api.security.anotation.RequireRoles;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PrescricaoService {

    private final PrescricaoRepository prescricaoRepository;
    private final ProntuarioService prontuarioService;

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional(readOnly = true)
    public List<PrescricaoResponseDTO> findAll() {
        return prescricaoRepository.findAll().stream().map(Prescricao::toResponseDTO).toList();
    }

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional(readOnly = true)
    public PrescricaoResponseDTO findById(Long id) {
        var prescricao = prescricaoRepository.findById(id).orElseThrow(() ->
                new InformacaoNaoEncontradaException(
                        Constantes.PRESCRICAO_NOT_FOUND_BY_ID_MESSAGE.replace("%s", String.valueOf(id))
                ));

        return Prescricao.toResponseDTO(prescricao);
    }

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PrescricaoResponseDTO save(PrescricaoRequestDTO prescricaoRequestDTO) {
        this.validarParametrosObrigatorios(prescricaoRequestDTO);

        if (verifyIfExistsByMedicamento(prescricaoRequestDTO.getMedicamento())) {
            throw new ConflitoException(Constantes.CONFLICT_MESSAGE);
        }

        var prontuario = prontuarioService.findById(prescricaoRequestDTO.getIdProntuario());
        var prescricao = Prescricao.builder()
                .medicamento(prescricaoRequestDTO.getMedicamento())
                .observacao(prescricaoRequestDTO.getObservacao())
                .dosagem(prescricaoRequestDTO.getDosagem())
                .duracao(prescricaoRequestDTO.getDuracao())
                .prontuario(Prontuario.toEntity(prontuario))
                .build();

        prescricao = prescricaoRepository.save(prescricao);
        return Prescricao.toResponseDTO(prescricao);
    }

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PrescricaoResponseDTO update(Long id, PrescricaoRequestDTO prescricaoRequestDTO) {
        this.validarParametrosObrigatorios(prescricaoRequestDTO);

        var prescricao = Prescricao.toEntity(this.findById(id));
        prescricao.setMedicamento(prescricaoRequestDTO.getMedicamento());
        prescricao.setObservacao(prescricaoRequestDTO.getObservacao());
        prescricao.setDosagem(prescricaoRequestDTO.getDosagem());
        prescricao.setDuracao(prescricaoRequestDTO.getDuracao());

        prescricao = prescricaoRepository.save(prescricao);
        return Prescricao.toResponseDTO(prescricao);
    }

    @RequireRoles({Constantes.LOGON_ROLE_ADMIN_SISTEMA})
    @Transactional
    public String delete(Long id) {
        var prescricao = prescricaoRepository.findById(id).orElseThrow(() ->
                new InformacaoNaoEncontradaException(
                        Constantes.PRESCRICAO_NOT_FOUND_BY_ID_MESSAGE.replace("%s", String.valueOf(id)))
        );

        var prontuario = prescricao.getProntuario();
        if (prontuario != null) {
            prontuario.getPrescricoes().removeIf(p -> p.getId().equals(id));
            prescricao.setProntuario(null);
        }

        prescricaoRepository.delete(prescricao);
        return "Prescrição deletada com sucesso.";
    }

    private boolean verifyIfExistsByMedicamento(String medicamento) {
        return prescricaoRepository.existsPrescricaoByMedicamento((medicamento));
    }

    private void validarParametrosObrigatorios(PrescricaoRequestDTO prescricaoRequestDTO) {
        if (prescricaoRequestDTO.getMedicamento() == null || prescricaoRequestDTO.getMedicamento().isEmpty()) {
            throw new ParametroInvalidoException("O campo Medicamento é obrigatório.");
        }
        if (prescricaoRequestDTO.getDosagem() == null || prescricaoRequestDTO.getDosagem().isEmpty()) {
            throw new ParametroInvalidoException("O campo Dosagem é obrigatório.");
        }
        if (prescricaoRequestDTO.getDuracao() == null || prescricaoRequestDTO.getDuracao().isEmpty()) {
            throw new ParametroInvalidoException("O campo Duração é obrigatório.");
        }
        if (prescricaoRequestDTO.getIdProntuario() == null) {
            throw new ParametroInvalidoException("O campo Id do prontuário é obrigatório.");
        }
    }
}

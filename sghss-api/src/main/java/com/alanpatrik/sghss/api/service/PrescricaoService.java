package com.alanpatrik.sghss.api.service;

import com.alanpatrik.sghss.api.dto.PrescricaoDTO;
import com.alanpatrik.sghss.api.model.Prescricao;
import com.alanpatrik.sghss.api.model.Prontuario;
import com.alanpatrik.sghss.api.repository.PrescricaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrescricaoService {

    @Autowired
    private PrescricaoRepository prescricaoRepository;

    @Autowired
    private ProntuarioService prontuarioService;

    public PrescricaoDTO findById(Long id) throws Exception {
        if (!verifyIfExistsById(id)) {
            throw new Exception("Prescrição não encontrada!");
        }
        return Prescricao.toDTO(prescricaoRepository.findById(id).get());
    }

    private boolean verifyIfExistsById(Long id) {
        return prescricaoRepository.existsById(id);
    }

    private boolean verifyIfExistsByMedicamento(String medicamento) {
        return prescricaoRepository.existsPrescricaoByMedicamento((medicamento));
    }

    public PrescricaoDTO findByMedicamento(String medicamento) {
        return Prescricao.toDTO(prescricaoRepository.findByMedicamento((medicamento)));
    }

    public PrescricaoDTO save(PrescricaoDTO prescricaoDTO) throws Exception {
        if (verifyIfExistsByMedicamento(prescricaoDTO.getMedicamento())) {
            throw new Exception("Prescrição já cadastrada!");
        }

        if (prontuarioService.findById(prescricaoDTO.getIdProntuario()) == null) {
            throw new Exception("Prontuário e ou paciente não encontrado!");
        }

        var prontuario = prontuarioService.findById(prescricaoDTO.getIdProntuario());
        var prescricao = Prescricao.builder()
                .medicamento(prescricaoDTO.getMedicamento())
                .observacao(prescricaoDTO.getObservacao())
                .dosagem(prescricaoDTO.getDosagem())
                .duracao(prescricaoDTO.getDuracao())
                .prontuario(Prontuario.toEntity(prontuario))
                .build();

        prescricao = prescricaoRepository.save(prescricao);
        return Prescricao.toDTO(prescricao);
    }

    public PrescricaoDTO update(Long id, PrescricaoDTO prescricaoDTO) throws Exception {
        if (!verifyIfExistsById(id)) {
            throw new Exception("Prescrição não encontrada!");
        }

        var prescricao = prescricaoRepository.findById(id).get();
        prescricao.setMedicamento(prescricaoDTO.getMedicamento());
        prescricao.setObservacao(prescricaoDTO.getObservacao());
        prescricao.setDosagem(prescricaoDTO.getDosagem());
        prescricao.setDuracao(prescricaoDTO.getDuracao());

        prescricao = prescricaoRepository.save(prescricao);
        return Prescricao.toDTO(prescricao);
    }
}

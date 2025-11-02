package com.alanpatrik.sghss.api.service;

import com.alanpatrik.sghss.api.dto.request.PrescricaoRequestDTO;
import com.alanpatrik.sghss.api.dto.response.PrescricaoResponseDTO;
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

    public PrescricaoResponseDTO findById(Long id) throws Exception {
        if (!verifyIfExistsById(id)) {
            throw new Exception("Prescrição não encontrada!");
        }
        return Prescricao.toResponseDTO(prescricaoRepository.findById(id).get());
    }

    private boolean verifyIfExistsById(Long id) {
        return prescricaoRepository.existsById(id);
    }

    private boolean verifyIfExistsByMedicamento(String medicamento) {
        return prescricaoRepository.existsPrescricaoByMedicamento((medicamento));
    }

    public PrescricaoResponseDTO findByMedicamento(String medicamento) {
        return Prescricao.toResponseDTO(prescricaoRepository.findByMedicamento((medicamento)));
    }

    public PrescricaoResponseDTO save(PrescricaoRequestDTO prescricaoRequestDTO) throws Exception {
        if (verifyIfExistsByMedicamento(prescricaoRequestDTO.getMedicamento())) {
            throw new Exception("Prescrição já cadastrada!");
        }

        if (prontuarioService.findById(prescricaoRequestDTO.getIdProntuario()) == null) {
            throw new Exception("Prontuário e ou paciente não encontrado!");
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

    public PrescricaoResponseDTO update(Long id, PrescricaoRequestDTO prescricaoRequestDTO) throws Exception {
        if (!verifyIfExistsById(id)) {
            throw new Exception("Prescrição não encontrada!");
        }

        var prescricao = prescricaoRepository.findById(id).get();
        prescricao.setMedicamento(prescricaoRequestDTO.getMedicamento());
        prescricao.setObservacao(prescricaoRequestDTO.getObservacao());
        prescricao.setDosagem(prescricaoRequestDTO.getDosagem());
        prescricao.setDuracao(prescricaoRequestDTO.getDuracao());

        prescricao = prescricaoRepository.save(prescricao);
        return Prescricao.toResponseDTO(prescricao);
    }
}

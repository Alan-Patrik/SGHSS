package com.alanpatrik.sghss.api.service;

import com.alanpatrik.sghss.api.model.Prescricao;
import com.alanpatrik.sghss.api.repository.PrescricaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrescricaoService {

    @Autowired
    private PrescricaoRepository prescricaoRepository;

    @Autowired
    private ProntuarioService prontuarioService;

    public Prescricao findById(Long id) throws Exception {
        if (!verifyIfExistsById(id)) {
            throw new Exception("Prescrição não encontrada!");
        }
        return prescricaoRepository.findById(id).get();
    }

    private boolean verifyIfExistsById(Long id) {
        return prescricaoRepository.existsById(id);
    }

    private boolean verifyIfContainsByObject(Prescricao prescricao) {
        return prescricaoRepository.findAll().contains(prescricao);
    }

    public Prescricao save(Long idProntuario, Prescricao prescricao) throws Exception {
        if (verifyIfContainsByObject(prescricao)) {
            throw new Exception("Prescrição já cadastrada!");
        }

        if (prontuarioService.findById(idProntuario) == null) {
            throw new Exception("Paciente não encontrado!");
        }

        var prontuario = prontuarioService.findById(idProntuario);
        var novoPrescricao = Prescricao.builder()
                .medicamento(prescricao.getMedicamento())
                .observacao(prescricao.getObservacao())
                .dosagem(prescricao.getDosagem())
                .duracao(prescricao.getDuracao())
                .prontuario(prontuario)
                .build();

        prescricaoRepository.save(novoPrescricao);
        return novoPrescricao;
    }

    public Prescricao update(Long id, Prescricao prescricao) throws Exception {
        if (!verifyIfExistsById(id)) {
            throw new Exception("Prescrição não encontrada!");
        }

        var prescricaoAtualizado = prescricaoRepository.findById(id).get();
        prescricaoAtualizado.setMedicamento(prescricao.getMedicamento());
        prescricaoAtualizado.setObservacao(prescricao.getObservacao());
        prescricaoAtualizado.setDosagem(prescricao.getDosagem());
        prescricaoAtualizado.setDuracao(prescricao.getDuracao());

        prescricaoRepository.save(prescricaoAtualizado);
        return prescricaoAtualizado;
    }
}

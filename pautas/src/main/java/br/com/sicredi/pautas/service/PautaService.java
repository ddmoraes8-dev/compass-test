package br.com.sicredi.pautas.service;

import br.com.sicredi.pautas.dto.PautaDTO;
import br.com.sicredi.pautas.entity.Pauta;
import br.com.sicredi.pautas.exception.RecursoNaoEncontradoException; // Importar
import br.com.sicredi.pautas.mapper.PautaMapper;
import br.com.sicredi.pautas.repository.PautaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PautaService {

    private final PautaRepository pautaRepository;
    private final PautaMapper pautaMapper;

    public PautaService(PautaRepository pautaRepository, PautaMapper pautaMapper) {
        this.pautaRepository = pautaRepository;
        this.pautaMapper = pautaMapper;
    }

    public List<Pauta> listarPautas() {
        return pautaRepository.findAll();
    }

    public PautaDTO criarPauta(String nome) {
        PautaDTO pautaDTO = PautaDTO.builder()
                .nome(nome)
                .dataCriacao(LocalDateTime.now())
                .build();

        final var pauta = pautaRepository.save(pautaMapper.toPauta(pautaDTO));

        return pautaMapper.toPautaDTO(pauta);
    }

    public PautaDTO buscarPorId(Long id) {
        Pauta pauta = pautaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pauta não encontrada com o ID: " + id)); // Lança a exceção personalizada

        return pautaMapper.toPautaDTO(pauta);
    }
}
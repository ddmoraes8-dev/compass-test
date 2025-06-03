package br.com.sicredi.pautas.service;

import br.com.sicredi.pautas.entity.Pauta;
import br.com.sicredi.pautas.repository.PautaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PautaService {

    private final PautaRepository pautaRepository;

    public PautaService(PautaRepository pautaRepository) {
        this.pautaRepository = pautaRepository;
    }

    public List<Pauta> listarPautas() {
        return pautaRepository.findAll();
    }

    public Pauta criarPauta(String nome) {
        Pauta pauta = new Pauta();
        pauta.setNome(nome);
        pauta.setDataCriacao(LocalDateTime.now());
        return pautaRepository.save(pauta);
    }

    public Pauta buscarPorId(Long id) {
        return pautaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pauta não encontrada"));
    }
}

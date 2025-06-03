package br.com.sicredi.pautas.service;

import br.com.sicredi.pautas.entity.ControleSessao;
import br.com.sicredi.pautas.entity.Pauta;
import br.com.sicredi.pautas.repository.ControleSessaoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ControleSessaoService {

    private final ControleSessaoRepository controleSessaoRepository;
    private final PautaService pautaService;

    public ControleSessaoService(ControleSessaoRepository controleSessaoRepository, PautaService pautaService) {
        this.controleSessaoRepository = controleSessaoRepository;
        this.pautaService = pautaService;
    }

    @Transactional
    public ControleSessao abrirSessao(Long pautaId, Integer duracaoEmMinutos) {
        Pauta pauta = pautaService.buscarPorId(pautaId);

        if (controleSessaoRepository.findByPautaId(pautaId) != null) {
            throw new RuntimeException("Sessão já aberta para esta pauta.");
        }

        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime fechamento = agora.plusMinutes(duracaoEmMinutos != null ? duracaoEmMinutos : 1);

        ControleSessao sessao = new ControleSessao();
        sessao.setPauta(pauta);
        sessao.setDataAbertura(agora);
        sessao.setDataFechamento(fechamento);

        return controleSessaoRepository.save(sessao);
    }

    public boolean isSessaoAberta(Long pautaId) {
        ControleSessao sessao = controleSessaoRepository.findByPautaId(pautaId);
        if (sessao == null) return false;

        LocalDateTime agora = LocalDateTime.now();
        return agora.isAfter(sessao.getDataAbertura()) && agora.isBefore(sessao.getDataFechamento());
    }
}

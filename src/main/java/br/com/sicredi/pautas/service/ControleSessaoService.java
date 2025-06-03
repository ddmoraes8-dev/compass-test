package br.com.sicredi.pautas.service;

import br.com.sicredi.pautas.dto.ControleSessaoDTO;
import br.com.sicredi.pautas.dto.PautaDTO;
import br.com.sicredi.pautas.entity.ControleSessao;
import br.com.sicredi.pautas.exception.SessaoJaAbertaException; // Importar
import br.com.sicredi.pautas.mapper.ControleSessaoMapper;
import br.com.sicredi.pautas.repository.ControleSessaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ControleSessaoService {

    private final ControleSessaoRepository controleSessaoRepository;
    private final PautaService pautaService;
    private final ControleSessaoMapper controleSessaoMapper;

    public ControleSessaoService(ControleSessaoRepository controleSessaoRepository, PautaService pautaService, ControleSessaoMapper controleSessaoMapper) {
        this.controleSessaoRepository = controleSessaoRepository;
        this.pautaService = pautaService;
        this.controleSessaoMapper = controleSessaoMapper;
    }

    @Transactional
    public ControleSessaoDTO abrirSessao(Long pautaId, Integer duracaoEmMinutos) {
        PautaDTO pauta = pautaService.buscarPorId(pautaId);

        if (controleSessaoRepository.findByPautaId(pautaId) != null) {
            throw new SessaoJaAbertaException("Sessão já aberta para esta pauta.");
        }

        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime fechamento = agora.plusMinutes(duracaoEmMinutos != null && duracaoEmMinutos >= 1 ? duracaoEmMinutos : 1);

        ControleSessaoDTO controleSessaoDTO = ControleSessaoDTO.builder()
                .dataAbertura(agora)
                .dataFechamento(fechamento)
                .pautaId(pauta.getId())
                .build();

        final var controleSessao = controleSessaoRepository.save(controleSessaoMapper.toControleSessao(controleSessaoDTO));

        return controleSessaoMapper.toControleSessaoDTO(controleSessao);
    }

    public boolean isSessaoAberta(Long pautaId) {
        ControleSessao sessao = controleSessaoRepository.findByPautaId(pautaId);
        if (sessao == null) return false;

        LocalDateTime agora = LocalDateTime.now();
        return agora.isAfter(sessao.getDataAbertura()) && agora.isBefore(sessao.getDataFechamento());
    }
}
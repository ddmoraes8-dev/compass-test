package br.com.sicredi.pautas.service;

import br.com.sicredi.pautas.entity.Pauta;
import br.com.sicredi.pautas.entity.Usuario;
import br.com.sicredi.pautas.entity.Voto;
import br.com.sicredi.pautas.repository.UsuarioRepository;
import br.com.sicredi.pautas.repository.VotoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class VotoService {

    private final VotoRepository votoRepository;
    private final ControleSessaoService controleSessaoService;
    private final UsuarioRepository usuarioRepository;
    private final PautaService pautaService;

    public VotoService(VotoRepository votoRepository,
                       ControleSessaoService controleSessaoService,
                       UsuarioRepository usuarioRepository,
                       PautaService pautaService) {
        this.votoRepository = votoRepository;
        this.controleSessaoService = controleSessaoService;
        this.usuarioRepository = usuarioRepository;
        this.pautaService = pautaService;
    }

    @Transactional
    public Voto votar(Long pautaId, String cpf, Boolean voto) {
        if (!controleSessaoService.isSessaoAberta(pautaId)) {
            throw new RuntimeException("Sessão fechada para esta pauta.");
        }

        if (votoRepository.existsByPautaIdAndUsuarioCpf(pautaId, cpf)) {
            throw new RuntimeException("Usuário já votou nesta pauta.");
        }

        Usuario usuario = usuarioRepository.findById(cpf)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        Pauta pauta = pautaService.buscarPorId(pautaId);

        Voto novoVoto = new Voto();
        novoVoto.setPauta(pauta);
        novoVoto.setUsuario(usuario);
        novoVoto.setVoto(voto);
        novoVoto.setDataVoto(LocalDateTime.now());

        return votoRepository.save(novoVoto);
    }

    public long contarVotosSim(Long pautaId) {
        return votoRepository.countByPautaIdAndVoto(pautaId, true);
    }

    public long contarVotosNao(Long pautaId) {
        return votoRepository.countByPautaIdAndVoto(pautaId, false);
    }
}


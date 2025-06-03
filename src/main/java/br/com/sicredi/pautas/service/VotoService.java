package br.com.sicredi.pautas.service;

import br.com.sicredi.pautas.dto.PautaDTO;
import br.com.sicredi.pautas.dto.UsuarioDTO;
import br.com.sicredi.pautas.dto.VotoDTO;
import br.com.sicredi.pautas.exception.RecursoNaoEncontradoException; // Importar nova exceção
import br.com.sicredi.pautas.exception.SessaoFechadaException;      // Importar nova exceção
import br.com.sicredi.pautas.exception.VotoDuplicadoException;      // Importar nova exceção
import br.com.sicredi.pautas.mapper.UsuarioMapper;
import br.com.sicredi.pautas.mapper.VotoMapper;
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
    private final VotoMapper votoMapper;
    private final UsuarioMapper usuarioMapper;

    public VotoService(VotoRepository votoRepository,
                       ControleSessaoService controleSessaoService,
                       UsuarioRepository usuarioRepository,
                       PautaService pautaService, VotoMapper votoMapper, UsuarioMapper usuarioMapper) {
        this.votoRepository = votoRepository;
        this.controleSessaoService = controleSessaoService;
        this.usuarioRepository = usuarioRepository;
        this.pautaService = pautaService;
        this.votoMapper = votoMapper;
        this.usuarioMapper = usuarioMapper;
    }

    @Transactional
    public VotoDTO votar(Long pautaId, String cpf, Boolean voto) {
        if (!controleSessaoService.isSessaoAberta(pautaId)) {
            throw new SessaoFechadaException("Sessão fechada para esta pauta.");
        }

        if (votoRepository.existsByPautaIdAndUsuarioCpf(pautaId, cpf)) {
            throw new VotoDuplicadoException("Usuário já votou nesta pauta.");
        }

        UsuarioDTO usuario = usuarioMapper.toUsuarioDTO(usuarioRepository.findById(cpf)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado.")));

        PautaDTO pauta = pautaService.buscarPorId(pautaId);

        var votoDTO = VotoDTO.builder()
                .pautaId(pauta.getId())
                .cpfUsuario(usuario.getCpf())
                .voto(voto)
                .dataVoto(LocalDateTime.now())
                .build();

        final var votoCadastrado = votoRepository.save(votoMapper.toVoto(votoDTO));
        return votoMapper.toVotoDTO(votoCadastrado);
    }

    public long contarVotosSim(Long pautaId) {
        return votoRepository.countByPautaIdAndVoto(pautaId, true);
    }

    public long contarVotosNao(Long pautaId) {
        return votoRepository.countByPautaIdAndVoto(pautaId, false);
    }
}
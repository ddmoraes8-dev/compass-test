package br.com.sicredi.pautas.service;

import br.com.sicredi.pautas.dto.UsuarioDTO;
import br.com.sicredi.pautas.entity.Usuario;
import br.com.sicredi.pautas.mapper.UsuarioMapper;
import br.com.sicredi.pautas.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    public UsuarioDTO criarUsuario(UsuarioDTO usuarioDTO) {
        final var usuario = usuarioRepository.save(usuarioMapper.toUsuario(usuarioDTO));
        return usuarioMapper.toUsuarioDTO(usuario);
    }
}


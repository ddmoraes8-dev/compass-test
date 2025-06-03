package br.com.sicredi.pautas.service;

import br.com.sicredi.pautas.dto.UsuarioDTO;
import br.com.sicredi.pautas.entity.Usuario;
import br.com.sicredi.pautas.mapper.UsuarioMapper;
import br.com.sicredi.pautas.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    @InjectMocks
    private UsuarioService usuarioService;

    private UsuarioDTO usuarioDTOEntrada;
    private Usuario usuarioEntitySalvo;
    private UsuarioDTO usuarioDTORetorno;

    @BeforeEach
    void setUp() {
        usuarioDTOEntrada = UsuarioDTO.builder()
                .nome("Maria Teste")
                .cpf("98765432100")
                .build();

        usuarioEntitySalvo = new Usuario();
        usuarioEntitySalvo.setNome("Maria Teste");
        usuarioEntitySalvo.setCpf("98765432100");

        usuarioDTORetorno = UsuarioDTO.builder()
                .nome("Maria Teste")
                .cpf("98765432100")
                .build();
    }

    @Test
    @DisplayName("Deve criar um novo usuário com sucesso")
    void deveCriarNovoUsuarioComSucesso() {

        when(usuarioMapper.toUsuario(any(UsuarioDTO.class))).thenReturn(usuarioEntitySalvo);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioEntitySalvo);
        when(usuarioMapper.toUsuarioDTO(any(Usuario.class))).thenReturn(usuarioDTORetorno);

        UsuarioDTO resultado = usuarioService.criarUsuario(usuarioDTOEntrada);

        assertNotNull(resultado);
        assertEquals(usuarioDTORetorno.getNome(), resultado.getNome());
        assertEquals(usuarioDTORetorno.getCpf(), resultado.getCpf());

        verify(usuarioMapper, times(1)).toUsuario(usuarioDTOEntrada);
        verify(usuarioRepository, times(1)).save(usuarioEntitySalvo);
        verify(usuarioMapper, times(1)).toUsuarioDTO(usuarioEntitySalvo);
    }
}
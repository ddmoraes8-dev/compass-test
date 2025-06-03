package br.com.sicredi.pautas.service;

import br.com.sicredi.pautas.dto.PautaDTO;
import br.com.sicredi.pautas.dto.UsuarioDTO;
import br.com.sicredi.pautas.dto.VotoDTO;
import br.com.sicredi.pautas.entity.Usuario;
import br.com.sicredi.pautas.entity.Voto;
import br.com.sicredi.pautas.exception.RecursoNaoEncontradoException;
import br.com.sicredi.pautas.exception.SessaoFechadaException;
import br.com.sicredi.pautas.exception.VotoDuplicadoException;
import br.com.sicredi.pautas.mapper.PautaMapper;
import br.com.sicredi.pautas.mapper.UsuarioMapper;
import br.com.sicredi.pautas.mapper.VotoMapper;
import br.com.sicredi.pautas.repository.UsuarioRepository;
import br.com.sicredi.pautas.repository.VotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VotoServiceTest {

    @Mock
    private VotoRepository votoRepository;
    @Mock
    private ControleSessaoService controleSessaoService;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private PautaService pautaService;
    @Mock
    private VotoMapper votoMapper;
    @Mock
    private UsuarioMapper usuarioMapper;
    @Mock
    private PautaMapper pautaMapper;

    @InjectMocks
    private VotoService votoService;

    private Long pautaId;
    private String cpfUsuario;
    private Boolean votoSim;
    private Boolean votoNao;
    private Usuario usuarioEntity;
    private UsuarioDTO usuarioDTO;
    private PautaDTO pautaDTO;
    private Voto votoEntitySim;
    private VotoDTO votoDTOSim;

    @BeforeEach
    void setUp() {
        pautaId = 1L;
        cpfUsuario = "12345678900";
        votoSim = true;
        votoNao = false;

        usuarioEntity = new Usuario();
        usuarioEntity.setCpf(cpfUsuario);
        usuarioEntity.setNome("Teste Associado");

        usuarioDTO = UsuarioDTO.builder()
                .cpf(cpfUsuario)
                .nome("Teste Associado")
                .build();

        pautaDTO = PautaDTO.builder()
                .id(pautaId)
                .nome("Pauta de Teste")
                .build();

        votoEntitySim = new Voto();
        votoEntitySim.setId(1L);
        votoEntitySim.setPauta(pautaMapper.toPauta(pautaDTO));
        votoEntitySim.setUsuario(usuarioMapper.toUsuario(usuarioDTO));
        votoEntitySim.setVoto(votoSim);
        votoEntitySim.setDataVoto(LocalDateTime.now());

        votoDTOSim = VotoDTO.builder()
                .id(1L)
                .pautaId(pautaId)
                .cpfUsuario(cpfUsuario)
                .voto(votoSim)
                .dataVoto(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Deve registrar um voto com sucesso quando a sessão está aberta e o usuário não votou")
    void deveRegistrarVotoComSucesso() {
        // Configurações dos mocks para o cenário de sucesso
        when(controleSessaoService.isSessaoAberta(pautaId)).thenReturn(true);
        when(votoRepository.existsByPautaIdAndUsuarioCpf(pautaId, cpfUsuario)).thenReturn(false);
        when(usuarioRepository.findById(cpfUsuario)).thenReturn(Optional.of(usuarioEntity));
        when(usuarioMapper.toUsuarioDTO(usuarioEntity)).thenReturn(usuarioDTO);
        when(pautaService.buscarPorId(pautaId)).thenReturn(pautaDTO);
        when(votoMapper.toVoto(any(VotoDTO.class))).thenReturn(votoEntitySim);
        when(votoRepository.save(any(Voto.class))).thenReturn(votoEntitySim);
        when(votoMapper.toVotoDTO(votoEntitySim)).thenReturn(votoDTOSim);

        VotoDTO resultado = votoService.votar(pautaId, cpfUsuario, votoSim);

        assertNotNull(resultado);
        assertEquals(votoDTOSim.getId(), resultado.getId());
        assertEquals(pautaId, resultado.getPautaId());
        assertEquals(cpfUsuario, resultado.getCpfUsuario());
        assertEquals(votoSim, resultado.getVoto());

        verify(controleSessaoService, times(1)).isSessaoAberta(pautaId);
        verify(votoRepository, times(1)).existsByPautaIdAndUsuarioCpf(pautaId, cpfUsuario);
        verify(usuarioRepository, times(1)).findById(cpfUsuario);
        verify(usuarioMapper, times(1)).toUsuarioDTO(usuarioEntity);
        verify(pautaService, times(1)).buscarPorId(pautaId);
        verify(votoMapper, times(1)).toVoto(any(VotoDTO.class));
        verify(votoRepository, times(1)).save(any(Voto.class));
        verify(votoMapper, times(1)).toVotoDTO(votoEntitySim);
    }

    @Test
    @DisplayName("Deve lançar SessaoFechadaException quando a sessão não está aberta")
    void deveLancarSessaoFechadaExceptionQuandoSessaoNaoAberta() {
        when(controleSessaoService.isSessaoAberta(pautaId)).thenReturn(false);

        SessaoFechadaException thrown = assertThrows(SessaoFechadaException.class, () ->
                votoService.votar(pautaId, cpfUsuario, votoSim));

        assertEquals("Sessão fechada para esta pauta.", thrown.getMessage());
        verify(controleSessaoService, times(1)).isSessaoAberta(pautaId);
        verify(votoRepository, never()).existsByPautaIdAndUsuarioCpf(anyLong(), anyString());
        verify(usuarioRepository, never()).findById(anyString());
        verify(pautaService, never()).buscarPorId(anyLong());
        verify(votoRepository, never()).save(any(Voto.class));
    }

    @Test
    @DisplayName("Deve lançar VotoDuplicadoException quando o usuário já votou")
    void deveLancarVotoDuplicadoExceptionQuandoUsuarioJaVotou() {
        when(controleSessaoService.isSessaoAberta(pautaId)).thenReturn(true);
        when(votoRepository.existsByPautaIdAndUsuarioCpf(pautaId, cpfUsuario)).thenReturn(true); // Usuário já votou

        VotoDuplicadoException thrown = assertThrows(VotoDuplicadoException.class, () ->
                votoService.votar(pautaId, cpfUsuario, votoSim));

        assertEquals("Usuário já votou nesta pauta.", thrown.getMessage());
        verify(controleSessaoService, times(1)).isSessaoAberta(pautaId);
        verify(votoRepository, times(1)).existsByPautaIdAndUsuarioCpf(pautaId, cpfUsuario);
        verify(usuarioRepository, never()).findById(anyString());
        verify(pautaService, never()).buscarPorId(anyLong());
        verify(votoRepository, never()).save(any(Voto.class));
    }

    @Test
    @DisplayName("Deve lançar RecursoNaoEncontradoException quando o usuário não é encontrado")
    void deveLancarRecursoNaoEncontradoExceptionQuandoUsuarioNaoEncontrado() {
        when(controleSessaoService.isSessaoAberta(pautaId)).thenReturn(true);
        when(votoRepository.existsByPautaIdAndUsuarioCpf(pautaId, cpfUsuario)).thenReturn(false);
        when(usuarioRepository.findById(cpfUsuario)).thenReturn(Optional.empty()); // Usuário não encontrado

        RecursoNaoEncontradoException thrown = assertThrows(RecursoNaoEncontradoException.class, () ->
                votoService.votar(pautaId, cpfUsuario, votoSim));

        assertEquals("Usuário não encontrado.", thrown.getMessage());
        verify(controleSessaoService, times(1)).isSessaoAberta(pautaId);
        verify(votoRepository, times(1)).existsByPautaIdAndUsuarioCpf(pautaId, cpfUsuario);
        verify(usuarioRepository, times(1)).findById(cpfUsuario);
        verify(pautaService, never()).buscarPorId(anyLong());
        verify(votoRepository, never()).save(any(Voto.class));
    }

    @Test
    @DisplayName("Deve contar corretamente os votos 'Sim'")
    void deveContarVotosSimCorretamente() {
        long votosSimEsperados = 5L;
        when(votoRepository.countByPautaIdAndVoto(pautaId, true)).thenReturn(votosSimEsperados);

        long resultado = votoService.contarVotosSim(pautaId);

        assertEquals(votosSimEsperados, resultado);
        verify(votoRepository, times(1)).countByPautaIdAndVoto(pautaId, true);
    }

    @Test
    @DisplayName("Deve contar corretamente os votos 'Não'")
    void deveContarVotosNaoCorretamente() {
        long votosNaoEsperados = 3L;
        when(votoRepository.countByPautaIdAndVoto(pautaId, false)).thenReturn(votosNaoEsperados);

        long resultado = votoService.contarVotosNao(pautaId);

        assertEquals(votosNaoEsperados, resultado);
        verify(votoRepository, times(1)).countByPautaIdAndVoto(pautaId, false);
    }

    @Test
    @DisplayName("Deve retornar zero para votos 'Sim' quando não houver nenhum")
    void deveRetornarZeroParaVotosSimQuandoNaoHouverNenhum() {
        when(votoRepository.countByPautaIdAndVoto(pautaId, true)).thenReturn(0L);

        long resultado = votoService.contarVotosSim(pautaId);

        assertEquals(0L, resultado);
        verify(votoRepository, times(1)).countByPautaIdAndVoto(pautaId, true);
    }

    @Test
    @DisplayName("Deve retornar zero para votos 'Não' quando não houver nenhum")
    void deveRetornarZeroParaVotosNaoQuandoNaoHouverNenhum() {
        when(votoRepository.countByPautaIdAndVoto(pautaId, false)).thenReturn(0L);

        long resultado = votoService.contarVotosNao(pautaId);

        assertEquals(0L, resultado);
        verify(votoRepository, times(1)).countByPautaIdAndVoto(pautaId, false);
    }
}
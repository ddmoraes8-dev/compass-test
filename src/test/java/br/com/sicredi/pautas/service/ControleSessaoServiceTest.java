package br.com.sicredi.pautas.service;

import br.com.sicredi.pautas.dto.ControleSessaoDTO;
import br.com.sicredi.pautas.dto.PautaDTO;
import br.com.sicredi.pautas.entity.ControleSessao;
import br.com.sicredi.pautas.exception.RecursoNaoEncontradoException;
import br.com.sicredi.pautas.exception.SessaoJaAbertaException;
import br.com.sicredi.pautas.mapper.ControleSessaoMapper;
import br.com.sicredi.pautas.mapper.PautaMapper;
import br.com.sicredi.pautas.repository.ControleSessaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ControleSessaoServiceTest {

    @Mock
    private ControleSessaoRepository controleSessaoRepository;
    @Mock
    private PautaService pautaService;
    @Mock
    private ControleSessaoMapper controleSessaoMapper;
    @Mock
    private PautaMapper pautaMapper;

    @InjectMocks
    private ControleSessaoService controleSessaoService;

    private Long pautaId;
    private Integer duracaoEmMinutos;
    private PautaDTO pautaDTO;
    private ControleSessao controleSessaoEntitySalva;
    private ControleSessaoDTO controleSessaoDTORetorno;

    @BeforeEach
    void setUp() {
        pautaId = 1L;
        duracaoEmMinutos = 5;

        pautaDTO = new PautaDTO(pautaId, "Pauta Teste Sessao", LocalDateTime.now());

        controleSessaoEntitySalva = new ControleSessao();
        controleSessaoEntitySalva.setId(1L);
        controleSessaoEntitySalva.setPauta(pautaMapper.toPauta(pautaDTO));
        controleSessaoEntitySalva.setDataAbertura(LocalDateTime.of(2025, 6, 3, 10, 0));
        controleSessaoEntitySalva.setDataFechamento(LocalDateTime.of(2025, 6, 3, 10, 5));

        controleSessaoDTORetorno = new ControleSessaoDTO(
                1L, pautaId,
                LocalDateTime.of(2025, 6, 3, 10, 0),
                LocalDateTime.of(2025, 6, 3, 10, 5));
    }

    @Test
    @DisplayName("Deve abrir uma sessão com sucesso com duração especificada")
    void deveAbrirSessaoComSucessoComDuracaoEspecificada() {
        when(pautaService.buscarPorId(pautaId)).thenReturn(pautaDTO);
        when(controleSessaoRepository.findByPautaId(pautaId)).thenReturn(null);
        when(controleSessaoMapper.toControleSessao(any(ControleSessaoDTO.class))).thenReturn(controleSessaoEntitySalva);
        when(controleSessaoRepository.save(any(ControleSessao.class))).thenReturn(controleSessaoEntitySalva);
        when(controleSessaoMapper.toControleSessaoDTO(any(ControleSessao.class))).thenReturn(controleSessaoDTORetorno);

        ControleSessaoDTO resultado = controleSessaoService.abrirSessao(pautaId, duracaoEmMinutos);

        assertNotNull(resultado);
        assertEquals(pautaId, resultado.getPautaId());
        assertNotNull(resultado.getDataAbertura());
        assertNotNull(resultado.getDataFechamento());
        assertTrue(resultado.getDataFechamento().isAfter(resultado.getDataAbertura()));
        assertTrue(resultado.getDataFechamento().isBefore(resultado.getDataAbertura().plusMinutes(duracaoEmMinutos + 1)));
        assertTrue(resultado.getDataFechamento().isAfter(resultado.getDataAbertura().plusMinutes(duracaoEmMinutos - 1)));

        verify(pautaService, times(1)).buscarPorId(pautaId);
        verify(controleSessaoRepository, times(1)).findByPautaId(pautaId);
        verify(controleSessaoMapper, times(1)).toControleSessao(any(ControleSessaoDTO.class));
        verify(controleSessaoRepository, times(1)).save(any(ControleSessao.class));
        verify(controleSessaoMapper, times(1)).toControleSessaoDTO(any(ControleSessao.class));
    }

    @Test
    @DisplayName("Deve abrir uma sessão com sucesso com duração padrão de 1 minuto se não especificada ou inválida")
    void deveAbrirSessaoComSucessoComDuracaoPadrao() {
        Integer duracaoNula = null;
        Integer duracaoZero = 0;
        Integer duracaoNegativa = -5;

        when(pautaService.buscarPorId(pautaId)).thenReturn(pautaDTO);
        when(controleSessaoRepository.findByPautaId(pautaId)).thenReturn(null);
        when(controleSessaoMapper.toControleSessao(any(ControleSessaoDTO.class))).thenReturn(controleSessaoEntitySalva);
        when(controleSessaoRepository.save(any(ControleSessao.class))).thenReturn(controleSessaoEntitySalva);
        when(controleSessaoMapper.toControleSessaoDTO(any(ControleSessao.class))).thenReturn(controleSessaoDTORetorno);

        ControleSessaoDTO resultado = controleSessaoService.abrirSessao(pautaId, duracaoNula);
        assertNotNull(resultado);
        assertTrue(resultado.getDataFechamento().isAfter(resultado.getDataAbertura().plusMinutes(0)));

        when(pautaService.buscarPorId(pautaId)).thenReturn(pautaDTO);
        when(controleSessaoRepository.findByPautaId(pautaId)).thenReturn(null);
        when(controleSessaoMapper.toControleSessao(any(ControleSessaoDTO.class))).thenReturn(controleSessaoEntitySalva);
        when(controleSessaoRepository.save(any(ControleSessao.class))).thenReturn(controleSessaoEntitySalva);
        when(controleSessaoMapper.toControleSessaoDTO(any(ControleSessao.class))).thenReturn(controleSessaoDTORetorno);

        resultado = controleSessaoService.abrirSessao(pautaId, duracaoZero);
        assertNotNull(resultado);
        assertTrue(resultado.getDataFechamento().isAfter(resultado.getDataAbertura().plusMinutes(0)));

        when(pautaService.buscarPorId(pautaId)).thenReturn(pautaDTO);
        when(controleSessaoRepository.findByPautaId(pautaId)).thenReturn(null);
        when(controleSessaoMapper.toControleSessao(any(ControleSessaoDTO.class))).thenReturn(controleSessaoEntitySalva);
        when(controleSessaoRepository.save(any(ControleSessao.class))).thenReturn(controleSessaoEntitySalva);
        when(controleSessaoMapper.toControleSessaoDTO(any(ControleSessao.class))).thenReturn(controleSessaoDTORetorno);

        resultado = controleSessaoService.abrirSessao(pautaId, duracaoNegativa);
        assertNotNull(resultado);
        assertTrue(resultado.getDataFechamento().isAfter(resultado.getDataAbertura().plusMinutes(0)));

        verify(pautaService, times(3)).buscarPorId(pautaId);
        verify(controleSessaoRepository, times(3)).findByPautaId(pautaId);
        verify(controleSessaoMapper, times(3)).toControleSessao(any(ControleSessaoDTO.class));
        verify(controleSessaoRepository, times(3)).save(any(ControleSessao.class));
        verify(controleSessaoMapper, times(3)).toControleSessaoDTO(any(ControleSessao.class));
    }


    @Test
    @DisplayName("Deve lançar RecursoNaoEncontradoException se a pauta não for encontrada ao abrir sessão")
    void deveLancarRecursoNaoEncontradoExceptionAoAbrirSessaoPautaNaoEncontrada() {
        when(pautaService.buscarPorId(pautaId)).thenThrow(new RecursoNaoEncontradoException("Pauta não encontrada."));

        RecursoNaoEncontradoException thrown = assertThrows(RecursoNaoEncontradoException.class, () ->
                controleSessaoService.abrirSessao(pautaId, duracaoEmMinutos));

        assertEquals("Pauta não encontrada.", thrown.getMessage());
        verify(pautaService, times(1)).buscarPorId(pautaId);
        verify(controleSessaoRepository, never()).findByPautaId(anyLong());
        verify(controleSessaoRepository, never()).save(any(ControleSessao.class));
    }

    @Test
    @DisplayName("Deve lançar SessaoJaAbertaException se já houver uma sessão para a pauta")
    void deveLancarSessaoJaAbertaExceptionQuandoSessaoJaAberta() {
        when(pautaService.buscarPorId(pautaId)).thenReturn(pautaDTO);
        when(controleSessaoRepository.findByPautaId(pautaId)).thenReturn(controleSessaoEntitySalva);

        SessaoJaAbertaException thrown = assertThrows(SessaoJaAbertaException.class, () ->
                controleSessaoService.abrirSessao(pautaId, duracaoEmMinutos));

        assertEquals("Sessão já aberta para esta pauta.", thrown.getMessage());
        verify(pautaService, times(1)).buscarPorId(pautaId);
        verify(controleSessaoRepository, times(1)).findByPautaId(pautaId);
        verify(controleSessaoRepository, never()).save(any(ControleSessao.class));
    }

    @Test
    @DisplayName("Deve retornar true se a sessão estiver aberta")
    void deveRetornarTrueQuandoSessaoEstiverAberta() {
        LocalDateTime agora = LocalDateTime.now();
        ControleSessao sessaoAberta = new ControleSessao();
        sessaoAberta.setDataAbertura(agora.minusMinutes(5));
        sessaoAberta.setDataFechamento(agora.plusMinutes(5));

        when(controleSessaoRepository.findByPautaId(pautaId)).thenReturn(sessaoAberta);

        assertTrue(controleSessaoService.isSessaoAberta(pautaId));
        verify(controleSessaoRepository, times(1)).findByPautaId(pautaId);
    }

    @Test
    @DisplayName("Deve retornar false se a sessão for nula (não existe)")
    void deveRetornarFalseQuandoSessaoForNula() {
        when(controleSessaoRepository.findByPautaId(pautaId)).thenReturn(null);

        assertFalse(controleSessaoService.isSessaoAberta(pautaId));
        verify(controleSessaoRepository, times(1)).findByPautaId(pautaId);
    }

    @Test
    @DisplayName("Deve retornar false se a sessão já estiver fechada")
    void deveRetornarFalseQuandoSessaoJaEstiverFechada() {
        LocalDateTime agora = LocalDateTime.now();
        ControleSessao sessaoFechada = new ControleSessao();
        sessaoFechada.setDataAbertura(agora.minusMinutes(10));
        sessaoFechada.setDataFechamento(agora.minusMinutes(5));

        when(controleSessaoRepository.findByPautaId(pautaId)).thenReturn(sessaoFechada);

        assertFalse(controleSessaoService.isSessaoAberta(pautaId));
        verify(controleSessaoRepository, times(1)).findByPautaId(pautaId);
    }

    @Test
    @DisplayName("Deve retornar false se a sessão ainda não abriu")
    void deveRetornarFalseQuandoSessaoAindaNaoAbriu() {
        LocalDateTime agora = LocalDateTime.now();
        ControleSessao sessaoNaoAberta = new ControleSessao();
        sessaoNaoAberta.setDataAbertura(agora.plusMinutes(5));
        sessaoNaoAberta.setDataFechamento(agora.plusMinutes(10));

        when(controleSessaoRepository.findByPautaId(pautaId)).thenReturn(sessaoNaoAberta);

        assertFalse(controleSessaoService.isSessaoAberta(pautaId));
        verify(controleSessaoRepository, times(1)).findByPautaId(pautaId);
    }
}
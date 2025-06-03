package br.com.sicredi.pautas.service;

import br.com.sicredi.pautas.dto.PautaDTO;
import br.com.sicredi.pautas.entity.Pauta;
import br.com.sicredi.pautas.exception.RecursoNaoEncontradoException;
import br.com.sicredi.pautas.mapper.PautaMapper;
import br.com.sicredi.pautas.repository.PautaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PautaServiceTest {

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private PautaMapper pautaMapper;

    @InjectMocks
    private PautaService pautaService;

    private Pauta pautaEntity1;
    private Pauta pautaEntity2;
    private PautaDTO pautaDTO1;
    private PautaDTO pautaDTOCriar;
    private Pauta pautaEntityCriada;
    private PautaDTO pautaDTOCriadaRetorno;

    @BeforeEach
    void setUp() {
        pautaEntity1 = new Pauta();
        pautaEntity1.setId(1L);
        pautaEntity1.setNome("Pauta Teste 1");
        pautaEntity1.setDataCriacao(LocalDateTime.now().minusDays(5));

        pautaEntity2 = new Pauta();
        pautaEntity2.setId(2L);
        pautaEntity2.setNome("Pauta Teste 2");
        pautaEntity2.setDataCriacao(LocalDateTime.now().minusDays(2));

        pautaDTO1 = new PautaDTO(1L, "Pauta Teste 1", LocalDateTime.now().minusDays(5));

        String nomeNovaPauta = "Nova Pauta para Teste";
        pautaDTOCriar = PautaDTO.builder()
                .nome(nomeNovaPauta)
                .dataCriacao(LocalDateTime.now())
                .build();

        pautaEntityCriada = new Pauta();
        pautaEntityCriada.setId(3L);
        pautaEntityCriada.setNome(nomeNovaPauta);
        pautaEntityCriada.setDataCriacao(LocalDateTime.now());

        pautaDTOCriadaRetorno = new PautaDTO(3L, nomeNovaPauta, LocalDateTime.now());
    }

    @Test
    @DisplayName("Deve retornar todas as pautas com sucesso")
    void deveListarTodasAsPautasComSucesso() {
        List<Pauta> pautasEsperadas = Arrays.asList(pautaEntity1, pautaEntity2);
        when(pautaRepository.findAll()).thenReturn(pautasEsperadas);

        List<Pauta> resultado = pautaService.listarPautas();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(pautasEsperadas, resultado);
        verify(pautaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve criar uma nova pauta com sucesso")
    void deveCriarNovaPautaComSucesso() {
        String nomePauta = "Pauta a ser criada";

        when(pautaMapper.toPauta(any(PautaDTO.class))).thenReturn(pautaEntityCriada);
        when(pautaRepository.save(any(Pauta.class))).thenReturn(pautaEntityCriada);
        when(pautaMapper.toPautaDTO(any(Pauta.class))).thenReturn(pautaDTOCriadaRetorno);

        PautaDTO resultado = pautaService.criarPauta(nomePauta);

        assertNotNull(resultado);
        assertEquals(pautaDTOCriadaRetorno.getId(), resultado.getId());
        assertEquals(pautaDTOCriadaRetorno.getNome(), resultado.getNome());
        verify(pautaMapper, times(1)).toPauta(any(PautaDTO.class));
        verify(pautaRepository, times(1)).save(any(Pauta.class));
        verify(pautaMapper, times(1)).toPautaDTO(any(Pauta.class));
    }

    @Test
    @DisplayName("Deve buscar pauta por ID com sucesso")
    void deveBuscarPautaPorIdComSucesso() {
        Long idBusca = 1L;
        when(pautaRepository.findById(idBusca)).thenReturn(Optional.of(pautaEntity1));
        when(pautaMapper.toPautaDTO(pautaEntity1)).thenReturn(pautaDTO1);

        PautaDTO resultado = pautaService.buscarPorId(idBusca);

        assertNotNull(resultado);
        assertEquals(pautaDTO1.getId(), resultado.getId());
        assertEquals(pautaDTO1.getNome(), resultado.getNome());
        verify(pautaRepository, times(1)).findById(idBusca);
        verify(pautaMapper, times(1)).toPautaDTO(pautaEntity1);
    }

    @Test
    @DisplayName("Deve lançar RecursoNaoEncontradoException ao buscar pauta por ID inexistente")
    void deveLancarRecursoNaoEncontradoExceptionAoBuscarPautaPorIdInexistente() {
        Long idInexistente = 99L;
        when(pautaRepository.findById(idInexistente)).thenReturn(Optional.empty());

        RecursoNaoEncontradoException thrown = assertThrows(RecursoNaoEncontradoException.class, () ->
                pautaService.buscarPorId(idInexistente));

        assertEquals("Pauta não encontrada com o ID: " + idInexistente, thrown.getMessage());
        verify(pautaRepository, times(1)).findById(idInexistente);
        verify(pautaMapper, never()).toPautaDTO(any(Pauta.class));
    }
}
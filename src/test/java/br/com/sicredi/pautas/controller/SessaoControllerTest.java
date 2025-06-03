package br.com.sicredi.pautas.controller;

import br.com.sicredi.pautas.dto.ControleSessaoDTO;
import br.com.sicredi.pautas.service.ControleSessaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessaoController.class)
class SessaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ControleSessaoService controleSessaoService;

    private ControleSessaoDTO controleSessaoDTO;

    @BeforeEach
    void setUp() {
        controleSessaoDTO = ControleSessaoDTO.builder()
                .id(10L)
                .pautaId(1L)
                .dataFechamento(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Deve abrir uma sessão com sucesso com duração padrão")
    void deveAbrirSessaoComDuracaoPadraoComSucesso() throws Exception {
        Long pautaId = 1L;
        when(controleSessaoService.abrirSessao(anyLong(), any())).thenReturn(controleSessaoDTO);

        mockMvc.perform(post("/api/sessoes/abrir")
                        .param("pautaId", String.valueOf(pautaId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pautaId", is(1)));

    }

    @Test
    @DisplayName("Deve abrir uma sessão com sucesso com duração especificada")
    void deveAbrirSessaoComDuracaoEspecificadaComSucesso() throws Exception {
        Long pautaId = 2L;
        Integer duracao = 20;

        ControleSessaoDTO sessaoComDuracaoDTO = ControleSessaoDTO.builder()
                .pautaId(pautaId)
                .id(1L)
                .dataFechamento(LocalDateTime.now().plusMinutes(duracao))
                .build();
        when(controleSessaoService.abrirSessao(pautaId, duracao)).thenReturn(sessaoComDuracaoDTO);

        mockMvc.perform(post("/api/sessoes/abrir")
                        .param("pautaId", String.valueOf(pautaId))
                        .param("duracaoEmMinutos", String.valueOf(duracao))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pautaId", is(2)));
    }

    @Test
    @DisplayName("Deve verificar se a sessão está aberta e retornar true")
    void deveVerificarSessaoAbertaRetornarTrue() throws Exception {
        Long pautaId = 3L;
        when(controleSessaoService.isSessaoAberta(pautaId)).thenReturn(true);

        mockMvc.perform(get("/api/sessoes/status")
                        .param("pautaId", String.valueOf(pautaId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));
    }

    @Test
    @DisplayName("Deve verificar se a sessão está aberta e retornar false")
    void deveVerificarSessaoAbertaRetornarFalse() throws Exception {
        Long pautaId = 4L;
        when(controleSessaoService.isSessaoAberta(pautaId)).thenReturn(false);

        mockMvc.perform(get("/api/sessoes/status")
                        .param("pautaId", String.valueOf(pautaId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(false)));
    }


    @Test
    @DisplayName("Deve retornar erro ao tentar abrir sessão para pauta inexistente")
    void deveRetornarErroAoAbrirSessaoParaPautaInexistente() throws Exception {
        Long pautaIdInexistente = 99L;

        when(controleSessaoService.abrirSessao(anyLong(), any()))
                .thenThrow(new RuntimeException("Pauta inexistente ou erro interno"));

        mockMvc.perform(post("/api/sessoes/abrir")
                        .param("pautaId", String.valueOf(pautaIdInexistente))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}
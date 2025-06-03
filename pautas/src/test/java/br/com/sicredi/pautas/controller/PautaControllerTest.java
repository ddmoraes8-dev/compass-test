package br.com.sicredi.pautas.controller;


import br.com.sicredi.pautas.dto.PautaDTO;
import br.com.sicredi.pautas.entity.Pauta;
import br.com.sicredi.pautas.exception.RecursoNaoEncontradoException;
import br.com.sicredi.pautas.service.PautaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PautaController.class)
class PautaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PautaService pautaService;

    private Pauta pauta1;
    private Pauta pauta2;
    private PautaDTO pautaDTO1;
    private PautaDTO pautaDTOCriada;

    @BeforeEach
    void setUp() {

        pauta1 = new Pauta();
        pauta1.setId(1L);
        pauta1.setNome("Pauta Teste 1");

        pauta2 = new Pauta();
        pauta2.setId(2L);
        pauta2.setNome("Pauta Teste 2");

        pautaDTO1 = PautaDTO.builder()
                .id(1L)
                .nome("Pauta Teste DTO")
                .build();

        pautaDTOCriada = PautaDTO.builder()
                .id(3L)
                .nome("Nova Pauta Criada")
                .build();
    }

    @Test
    @DisplayName("Deve listar todas as pautas com sucesso")
    void deveListarTodasAsPautasComSucesso() throws Exception {
        List<Pauta> pautas = Arrays.asList(pauta1, pauta2);
        when(pautaService.listarPautas()).thenReturn(pautas);

        mockMvc.perform(get("/api/pautas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nome", is("Pauta Teste 1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].nome", is("Pauta Teste 2")));
    }

    @Test
    @DisplayName("Deve criar uma nova pauta com sucesso")
    void deveCriarUmaNovaPautaComSucesso() throws Exception {
        String nomeNovaPauta = "Nova Pauta";
        when(pautaService.criarPauta(anyString())).thenReturn(pautaDTOCriada);

        mockMvc.perform(post("/api/pautas")
                        .param("nome", nomeNovaPauta)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.nome", is("Nova Pauta Criada")));
    }

    @Test
    @DisplayName("Deve buscar uma pauta por ID com sucesso")
    void deveBuscarUmaPautaPorIdComSucesso() throws Exception {
        Long idBusca = 1L;
        when(pautaService.buscarPorId(anyLong())).thenReturn(pautaDTO1);

        mockMvc.perform(get("/api/pautas/{id}", idBusca)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("Pauta Teste DTO")));
    }

    @Test
    @DisplayName("Deve retornar status 404 ao buscar pauta por ID inexistente")
    void deveRetornarNotFoundAoBuscarPautaPorIdInexistente() throws Exception {
        when(pautaService.buscarPorId(anyLong())).thenThrow(new RecursoNaoEncontradoException("Pauta não encontrada"));

        mockMvc.perform(get("/api/pautas/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}

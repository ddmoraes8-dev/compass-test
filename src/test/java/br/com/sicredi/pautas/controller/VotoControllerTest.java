package br.com.sicredi.pautas.controller;

import br.com.sicredi.pautas.dto.VotoDTO;
import br.com.sicredi.pautas.exception.SessaoFechadaException;
import br.com.sicredi.pautas.exception.VotoDuplicadoException;
import br.com.sicredi.pautas.exception.RecursoNaoEncontradoException;
import br.com.sicredi.pautas.service.VotoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VotoController.class)
class VotoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VotoService votoService;

    private VotoDTO votoDTO;

    @BeforeEach
    void setUp() {
        votoDTO = VotoDTO.builder()
                .id(1L)
                .pautaId(1L)
                .cpfUsuario("12345678900")
                .voto(true)
                .build();
    }

    @Test
    @DisplayName("Deve registrar um voto com sucesso")
    void deveRegistrarVotoComSucesso() throws Exception {
        Long pautaId = 1L;
        String cpf = "12345678900";
        Boolean voto = true;

        when(votoService.votar(pautaId, cpf, voto)).thenReturn(votoDTO);

        mockMvc.perform(post("/api/votos")
                        .param("pautaId", String.valueOf(pautaId))
                        .param("cpf", cpf)
                        .param("voto", String.valueOf(voto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.pautaId", is(1)))
                .andExpect(jsonPath("$.cpfUsuario", is("12345678900")))
                .andExpect(jsonPath("$.voto", is(true)));
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request ao tentar votar em sessão fechada")
    void deveRetornarBadRequestAoVotarEmSessaoFechada() throws Exception {
        Long pautaId = 2L;
        String cpf = "11122233344";
        Boolean voto = false;

        when(votoService.votar(anyLong(), anyString(), anyBoolean()))
                .thenThrow(new SessaoFechadaException("Sessão de votação para a pauta " + pautaId + " está fechada."));

        mockMvc.perform(post("/api/votos")
                        .param("pautaId", String.valueOf(pautaId))
                        .param("cpf", cpf)
                        .param("voto", String.valueOf(voto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 409 Conflict ao tentar registrar voto duplicado")
    void deveRetornarConflictAoRegistrarVotoDuplicado() throws Exception {
        Long pautaId = 3L;
        String cpf = "99988877766";
        Boolean voto = true;

        when(votoService.votar(anyLong(), anyString(), anyBoolean()))
                .thenThrow(new VotoDuplicadoException("Associado " + cpf + " já votou nesta pauta."));

        mockMvc.perform(post("/api/votos")
                        .param("pautaId", String.valueOf(pautaId))
                        .param("cpf", cpf)
                        .param("voto", String.valueOf(voto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found ao tentar votar em pauta inexistente")
    void deveRetornarNotFoundAoVotarEmPautaInexistente() throws Exception {
        Long pautaIdInexistente = 99L;
        String cpf = "55544433322";
        Boolean voto = true;

        when(votoService.votar(anyLong(), anyString(), anyBoolean()))
                .thenThrow(new RecursoNaoEncontradoException("Pauta não encontrada com o ID: " + pautaIdInexistente));

        mockMvc.perform(post("/api/votos")
                        .param("pautaId", String.valueOf(pautaIdInexistente))
                        .param("cpf", cpf)
                        .param("voto", String.valueOf(voto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Espera status 404 Not Found
    }

    @Test
    @DisplayName("Deve consultar o resultado dos votos com sucesso")
    void deveConsultarResultadoDosVotosComSucesso() throws Exception {
        Long pautaId = 4L;
        long votosSim = 10L;
        long votosNao = 5L;

        when(votoService.contarVotosSim(pautaId)).thenReturn(votosSim);
        when(votoService.contarVotosNao(pautaId)).thenReturn(votosNao);

        mockMvc.perform(get("/api/votos/resultado")
                        .param("pautaId", String.valueOf(pautaId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Espera status HTTP 200 OK
                .andExpect(jsonPath("$.SIM", is(10)))
                .andExpect(jsonPath("$.NAO", is(5)));
    }

    @Test
    @DisplayName("Deve consultar o resultado dos votos para pauta sem votos")
    void deveConsultarResultadoDosVotosParaPautaSemVotos() throws Exception {
        Long pautaId = 5L;
        long votosSim = 0L;
        long votosNao = 0L;

        when(votoService.contarVotosSim(pautaId)).thenReturn(votosSim);
        when(votoService.contarVotosNao(pautaId)).thenReturn(votosNao);

        mockMvc.perform(get("/api/votos/resultado")
                        .param("pautaId", String.valueOf(pautaId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.SIM", is(0)))
                .andExpect(jsonPath("$.NAO", is(0)));
    }
}
package br.com.sicredi.pautas.controller;

import br.com.sicredi.pautas.dto.UsuarioDTO;
import br.com.sicredi.pautas.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private UsuarioDTO usuarioDTOEntrada;
    private UsuarioDTO usuarioDTOSalvo;

    @BeforeEach
    void setUp() {

        usuarioDTOEntrada =  UsuarioDTO.builder()
                .nome("João Silva")
                .cpf(null)
                .build();

        usuarioDTOSalvo = UsuarioDTO.builder()
                .nome("João Silva")
                .cpf("12345678900")
                .build();
    }

    @Test
    @DisplayName("Deve criar um novo usuário com sucesso")
    void deveCriarNovoUsuarioComSucesso() throws Exception {
        when(usuarioService.criarUsuario(any(UsuarioDTO.class))).thenReturn(usuarioDTOSalvo);

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDTOEntrada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("João Silva")))
                .andExpect(jsonPath("$.cpf", is("12345678900")));
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar criar usuário com corpo da requisição vazio")
    void deveRetornarErroAoCriarUsuarioComCorpoVazio() throws Exception {
        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }
}
package br.com.sicredi.pautas.controller;

import br.com.sicredi.pautas.entity.Usuario;
import br.com.sicredi.pautas.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuário", description = "Cadastro de usuários (associados)")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Criar um novo usuário")
    @PostMapping
    public Usuario criar(@RequestBody Usuario usuario) {
        return usuarioService.criarUsuario(usuario);
    }
}



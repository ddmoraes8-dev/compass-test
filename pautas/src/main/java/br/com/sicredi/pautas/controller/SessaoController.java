package br.com.sicredi.pautas.controller;

import br.com.sicredi.pautas.dto.ControleSessaoDTO;
import br.com.sicredi.pautas.entity.ControleSessao;
import br.com.sicredi.pautas.service.ControleSessaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sessoes")
@Tag(name = "Sessão", description = "Gerenciamento da sessão de votação da pauta")
public class SessaoController {

    private final ControleSessaoService controleSessaoService;

    public SessaoController(ControleSessaoService controleSessaoService) {
        this.controleSessaoService = controleSessaoService;
    }

    @Operation(summary = "Abrir uma sessão para uma pauta")
    @PostMapping("/abrir")
    public ControleSessaoDTO abrirSessao(
            @RequestParam Long pautaId,
            @RequestParam(required = false) Integer duracaoEmMinutos
    ) {
        return controleSessaoService.abrirSessao(pautaId, duracaoEmMinutos);
    }

    @Operation(summary = "Verificar se a sessão está aberta para uma pauta")
    @GetMapping("/status")
    public boolean verificarSessaoAberta(@RequestParam Long pautaId) {
        return controleSessaoService.isSessaoAberta(pautaId);
    }
}



package br.com.sicredi.pautas.controller;

import br.com.sicredi.pautas.entity.Pauta;
import br.com.sicredi.pautas.service.PautaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pautas")
@Tag(name = "Pauta", description = "Endpoints para gerenciar pautas")
public class PautaController {

    private final PautaService pautaService;

    public PautaController(PautaService pautaService) {
        this.pautaService = pautaService;
    }

    @Operation(summary = "Listar todas as pautas")
    @GetMapping
    public List<Pauta> listar() {
        return pautaService.listarPautas();
    }

    @Operation(summary = "Criar uma nova pauta")
    @PostMapping
    public Pauta criar(@RequestParam String nome) {
        return pautaService.criarPauta(nome);
    }

    @Operation(summary = "Buscar uma pauta por ID")
    @GetMapping("/{id}")
    public Pauta buscarPorId(@PathVariable Long id) {
        return pautaService.buscarPorId(id);
    }
}

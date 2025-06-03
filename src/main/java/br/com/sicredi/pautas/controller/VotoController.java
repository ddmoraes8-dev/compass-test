package br.com.sicredi.pautas.controller;

import br.com.sicredi.pautas.dto.VotoDTO;
import br.com.sicredi.pautas.entity.Voto;
import br.com.sicredi.pautas.service.VotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/votos")
@Tag(name = "Voto", description = "Gerenciamento dos votos nas pautas")
public class VotoController {

    private final VotoService votoService;

    public VotoController(VotoService votoService) {
        this.votoService = votoService;
    }

    @Operation(summary = "Registrar um voto em uma pauta")
    @PostMapping
    public VotoDTO votar(
            @RequestParam Long pautaId,
            @RequestParam String cpf,
            @RequestParam Boolean voto
    ) {
        return votoService.votar(pautaId, cpf, voto);
    }

    @Operation(summary = "Consultar o resultado dos votos de uma pauta")
    @GetMapping("/resultado")
    public Map<String, Long> resultado(@RequestParam Long pautaId) {
        long sim = votoService.contarVotosSim(pautaId);
        long nao = votoService.contarVotosNao(pautaId);

        Map<String, Long> resultado = new HashMap<>();
        resultado.put("SIM", sim);
        resultado.put("NAO", nao);

        return resultado;
    }
}

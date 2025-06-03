package br.com.sicredi.pautas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ControleSessaoDTO {

    private Long id;
    private Long pautaId;
    private LocalDateTime dataAbertura;
    private LocalDateTime dataFechamento;
}

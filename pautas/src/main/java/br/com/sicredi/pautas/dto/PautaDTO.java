package br.com.sicredi.pautas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PautaDTO {

    private Long id;
    private String nome;
    private LocalDateTime dataCriacao;
}


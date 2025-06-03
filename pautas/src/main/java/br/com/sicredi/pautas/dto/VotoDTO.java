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
public class VotoDTO {

    private Long id;
    private Long pautaId;
    private String cpfUsuario;
    private Boolean voto;
    private LocalDateTime dataVoto;
}


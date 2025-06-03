package br.com.sicredi.pautas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

     @NotBlank(message = "O nome é obrigatório")
     private String nome;

     @NotBlank(message = "O CPF é obrigatório")
     @Pattern(regexp = "\\d{11}", message = "O CPF deve conter 11 dígitos numéricos")
     private String cpf;
}

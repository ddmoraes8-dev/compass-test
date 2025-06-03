package br.com.sicredi.pautas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "voto", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"pauta_id", "cpf_usuario"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pauta_id", nullable = false)
    private Pauta pauta;

    @ManyToOne
    @JoinColumn(name = "cpf_usuario", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private Boolean voto; // true = SIM, false = NÃO

    @Column(name = "data_voto", nullable = false)
    private LocalDateTime dataVoto = LocalDateTime.now();
}


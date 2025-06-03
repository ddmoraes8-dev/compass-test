package br.com.sicredi.pautas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "pauta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pauta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;
}

package br.com.sicredi.pautas.repository;

import br.com.sicredi.pautas.entity.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {
    boolean existsByPautaIdAndUsuarioCpf(Long pautaId, String cpf);
    long countByPautaIdAndVoto(Long pautaId, Boolean voto);
}

package br.com.sicredi.pautas.repository;

import br.com.sicredi.pautas.entity.ControleSessao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ControleSessaoRepository extends JpaRepository<ControleSessao, Long> {
    ControleSessao findByPautaId(Long pautaId);
}

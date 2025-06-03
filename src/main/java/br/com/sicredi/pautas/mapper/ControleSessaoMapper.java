package br.com.sicredi.pautas.mapper;

import br.com.sicredi.pautas.dto.ControleSessaoDTO;
import br.com.sicredi.pautas.entity.ControleSessao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ControleSessaoMapper {

    ControleSessaoMapper INSTANCE = Mappers.getMapper(ControleSessaoMapper.class);

    @Mapping(source = "pauta.id", target = "pautaId")
    ControleSessaoDTO toControleSessaoDTO(ControleSessao controleSessao);

    @Mapping(source = "pautaId", target = "pauta.id")
    ControleSessao toControleSessao(ControleSessaoDTO controleSessaoDTO);
}


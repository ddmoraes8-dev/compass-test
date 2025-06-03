package br.com.sicredi.pautas.mapper;

import br.com.sicredi.pautas.dto.PautaDTO;
import br.com.sicredi.pautas.entity.Pauta;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PautaMapper {

    PautaMapper INSTANCE = Mappers.getMapper(PautaMapper.class);

    PautaDTO toPautaDTO(Pauta pauta);
    Pauta toPauta(PautaDTO pautaDTO);
}

package br.com.sicredi.pautas.mapper;

import br.com.sicredi.pautas.dto.VotoDTO;
import br.com.sicredi.pautas.entity.Voto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface VotoMapper {

    VotoMapper INSTANCE = Mappers.getMapper(VotoMapper.class);

    @Mapping(source = "pauta.id", target = "pautaId")
    @Mapping(source = "usuario.cpf", target = "cpfUsuario")
    VotoDTO toVotoDTO(Voto voto);

    @Mapping(source = "pautaId", target = "pauta.id")
    @Mapping(source = "cpfUsuario", target = "usuario.cpf")
    Voto toVoto(VotoDTO votoDTO);
}


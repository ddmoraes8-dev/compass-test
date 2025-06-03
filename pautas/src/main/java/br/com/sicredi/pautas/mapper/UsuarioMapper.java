package br.com.sicredi.pautas.mapper;

import br.com.sicredi.pautas.dto.UsuarioDTO;
import br.com.sicredi.pautas.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    UsuarioDTO toUsuarioDTO(Usuario usuario);
    Usuario toUsuario(UsuarioDTO usuarioDTO);
}


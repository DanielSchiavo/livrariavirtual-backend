package br.com.danielschiavo.livrariavirtual.aplicacao;

import br.com.danielschiavo.livrariavirtual.modelo.Usuario;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    public Usuario cadastrarUsuarioDTOParaUsuario(CadastrarUsuarioDTO usuarioDTO);
}

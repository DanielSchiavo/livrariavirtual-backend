package br.com.danielschiavo.livrariavirtual.aplicacao;

import br.com.danielschiavo.livrariavirtual.dto.CadastrarUsuarioDTO;
import br.com.danielschiavo.livrariavirtual.modelo.RepositorioDeUsuario;
import br.com.danielschiavo.livrariavirtual.modelo.Usuario;
import org.springframework.stereotype.Service;

@Service
public class CadastrarUsuario {

    private RepositorioDeUsuario repositorioUsuario;

    private GeradorUUID geradorUUID;

    private UsuarioMapper usuarioMapper;

    public CadastrarUsuario(RepositorioDeUsuario repositorioDeUsuario, GeradorUUID geradorUUID, UsuarioMapper usuarioMapper) {
        this.repositorioUsuario = repositorioDeUsuario;
        this.geradorUUID = geradorUUID;
        this.usuarioMapper = usuarioMapper;
    }

    public Usuario executa(CadastrarUsuarioDTO cadastrarUsuarioDTO) {
        Usuario usuario = usuarioMapper.cadastrarUsuarioDTOParaUsuario(cadastrarUsuarioDTO);
        usuario.setId(geradorUUID.gerar());

        repositorioUsuario.cadastrarUsuario(usuario);

//        return "Cadastrado com sucesso";
        return usuario;
    }
}

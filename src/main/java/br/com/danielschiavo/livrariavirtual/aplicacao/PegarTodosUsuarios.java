package br.com.danielschiavo.livrariavirtual.aplicacao;

import br.com.danielschiavo.livrariavirtual.modelo.RepositorioDeUsuario;
import br.com.danielschiavo.livrariavirtual.modelo.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PegarTodosUsuarios {

    private RepositorioDeUsuario repositorioUsuario;

    public PegarTodosUsuarios(RepositorioDeUsuario repositorioDeUsuario) {
        this.repositorioUsuario = repositorioDeUsuario;
    }

    public List<Usuario> executa() {
        return repositorioUsuario.pegarTodosUsuarios();
    }
}

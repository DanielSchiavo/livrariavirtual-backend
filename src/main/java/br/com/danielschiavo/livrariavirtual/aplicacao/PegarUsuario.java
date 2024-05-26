package br.com.danielschiavo.livrariavirtual.aplicacao;

import br.com.danielschiavo.livrariavirtual.modelo.RepositorioDeUsuario;
import br.com.danielschiavo.livrariavirtual.modelo.Usuario;
import org.springframework.stereotype.Service;

@Service
public class PegarUsuario {

    private RepositorioDeUsuario repositorioUsuario;

    public PegarUsuario(RepositorioDeUsuario repositorioDeUsuario) {
        this.repositorioUsuario = repositorioDeUsuario;
    }

    public Usuario executa() {
        return repositorioUsuario.pegarUsuario();
    }
}

package br.com.danielschiavo.livrariavirtual.aplicacao;

import br.com.danielschiavo.livrariavirtual.modelo.Ebook;
import br.com.danielschiavo.livrariavirtual.modelo.Usuario;

import java.util.List;

public interface UsuarioPresenter {

    Usuario respostaPegarUsuario(Usuario usuario);

    String respostaAdicionarEbooks(List<Ebook> ebooks);

    String respostaCadastrarUsuario(Usuario usuario);
}

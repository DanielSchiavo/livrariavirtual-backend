package br.com.danielschiavo.livrariavirtual.infra;

import br.com.danielschiavo.livrariavirtual.aplicacao.UsuarioProtobufAdapter;
import br.com.danielschiavo.livrariavirtual.modelo.Ebook;
import br.com.danielschiavo.livrariavirtual.modelo.RepositorioDeUsuario;
import br.com.danielschiavo.livrariavirtual.modelo.Usuario;
import br.com.danielschiavo.livrariavirtual.modelo.UsuarioNaoEncontradoException;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RepositorioDeUsuarioComProtobuffer implements RepositorioDeUsuario {

    private static final String caminhoRepositorio = "dados.bin";

    private final UsuarioProtobufAdapter usuarioProtobufAdapter;

    public RepositorioDeUsuarioComProtobuffer(UsuarioProtobufAdapter usuarioProtobufAdapter) {
        this.usuarioProtobufAdapter = usuarioProtobufAdapter;
    }

    @Override
    public Usuario pegarUsuario() throws UsuarioNaoEncontradoException {
        br.com.danielschiavo.livrariavirtual.protos.Usuario usuario = getUsuario().build();
        return usuarioProtobufAdapter.fromProto(usuario);
    }

    @Override
    public void cadastrarUsuario(Usuario usuario) {
        br.com.danielschiavo.livrariavirtual.protos.Usuario novoUsuarioProto = usuarioProtobufAdapter.toProto(usuario);

        salvar(novoUsuarioProto.toBuilder());
    }

    @Override
    public void adicionarEbook(List<Ebook> ebooks, String usuarioId) throws UsuarioNaoEncontradoException {
        br.com.danielschiavo.livrariavirtual.protos.Usuario.Builder usuarioProtoBuilder = getUsuario();

        List<br.com.danielschiavo.livrariavirtual.protos.Ebook> ebooksProto = usuarioProtobufAdapter.toProto(ebooks);

        usuarioProtoBuilder.addAllEbooks(ebooksProto);
        salvar(usuarioProtoBuilder);
    }

    @Override
    public void removerEbook(Ebook ebook, String usuarioId) throws UsuarioNaoEncontradoException {
        br.com.danielschiavo.livrariavirtual.protos.Usuario.Builder usuarioProtoBuilder = getUsuario();
        br.com.danielschiavo.livrariavirtual.protos.Ebook ebookProto = usuarioProtobufAdapter.toProto(ebook);

        int indexEbookProto = usuarioProtoBuilder.getEbooksList().indexOf(ebookProto);
        usuarioProtoBuilder.removeEbooks(indexEbookProto);

        salvar(usuarioProtoBuilder);
    }

    @Override
    public List<Ebook> pegarEbookPorId(List<String> ebooksId, String usuarioId) {
        br.com.danielschiavo.livrariavirtual.protos.Usuario.Builder usuario = getUsuario();
        List<br.com.danielschiavo.livrariavirtual.protos.Ebook> ebooksList = usuario.getEbooksList();

        List<br.com.danielschiavo.livrariavirtual.protos.Ebook> ebooksFiltrados = ebooksList.stream()
                .filter(ebook -> ebooksId.contains(ebook.getId()))
                .collect(Collectors.toList());

        return usuarioProtobufAdapter.fromProto(ebooksFiltrados);
    }

    private void salvar(br.com.danielschiavo.livrariavirtual.protos.Usuario.Builder usuarioProtoBuilder) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(caminhoRepositorio);
            usuarioProtoBuilder.build().writeTo(fileOutputStream);
            fileOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao serializar e adicionar o usuário ao arquivo", e);
        }
    }

    private br.com.danielschiavo.livrariavirtual.protos.Usuario.Builder getUsuario() throws UsuarioNaoEncontradoException {
        System.out.println("Tentando ler o arquivo em: " + caminhoRepositorio);
        try {
            br.com.danielschiavo.livrariavirtual.protos.Usuario.Builder builder = br.com.danielschiavo.livrariavirtual.protos.Usuario.newBuilder();
            builder.mergeFrom(new FileInputStream(caminhoRepositorio));
            System.out.println("Usuário recuperado com sucesso: " + builder.build());
            System.out.println("Usuário recuperado com sucesso: ");
            return builder;
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
            e.printStackTrace();
            throw new UsuarioNaoEncontradoException("Usuario não cadastrado");
        }
    }
}

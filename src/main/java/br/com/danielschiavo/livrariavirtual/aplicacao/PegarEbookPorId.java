package br.com.danielschiavo.livrariavirtual.aplicacao;

import br.com.danielschiavo.livrariavirtual.infra.RepositorioDeUsuarioComProtobuffer;
import br.com.danielschiavo.livrariavirtual.modelo.Ebook;
import br.com.danielschiavo.livrariavirtual.modelo.RepositorioDeUsuario;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PegarEbookPorId {

    private RepositorioDeUsuario repositorioDeUsuario;

    public PegarEbookPorId(RepositorioDeUsuarioComProtobuffer repositorioDeUsuarioComProtobuffer) {
        this.repositorioDeUsuario = repositorioDeUsuarioComProtobuffer;
    }

    public List<Ebook> executa(List<UUID> ebooksId, UUID usuarioId) {
        List<String> ebooksIdString = ebooksId.stream().map(ebook -> ebook.toString()).collect(Collectors.toList());

        return repositorioDeUsuario.pegarEbookPorId(ebooksIdString, usuarioId.toString());
    }
 }

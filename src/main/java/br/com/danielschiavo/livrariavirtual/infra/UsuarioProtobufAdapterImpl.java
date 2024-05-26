package br.com.danielschiavo.livrariavirtual.infra;

import br.com.danielschiavo.livrariavirtual.aplicacao.UsuarioProtobufAdapter;
import br.com.danielschiavo.livrariavirtual.protos.Ebook;
import br.com.danielschiavo.livrariavirtual.modelo.Usuario;
import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsuarioProtobufAdapterImpl implements UsuarioProtobufAdapter {

    @Override
    public br.com.danielschiavo.livrariavirtual.protos.Usuario toProto(Usuario usuario) {
        return br.com.danielschiavo.livrariavirtual.protos.Usuario
                                    .newBuilder()
                                    .setId(usuario.getId().toString())
                                    .setNome(usuario.getNome())
                                    .setEmail(usuario.getEmail())
                                    .build();
    }

    @Override
    public Usuario fromProto(br.com.danielschiavo.livrariavirtual.protos.Usuario usuarioProto) {
        List<br.com.danielschiavo.livrariavirtual.modelo.Ebook> ebooks = fromProto(usuarioProto.getEbooksList());
        return Usuario.builder().id(UUID.fromString(usuarioProto.getId()))
                                .nome(usuarioProto.getNome())
                                .email(usuarioProto.getEmail())
                                .ebooks(ebooks)
                                .build();
    }

    @Override
    public Ebook toProto(br.com.danielschiavo.livrariavirtual.modelo.Ebook ebook) {
        Instant instant = ebook.getDataEHoraAdicao().toInstant(ZoneOffset.ofHours(0));
        Ebook.Builder ebookProtoBuilder = Ebook.newBuilder().setNome(ebook.getNome())
                .setNomeArquivo(ebook.getNomeArquivo())
                .setImagemCapa(ByteString.copyFrom(ebook.getImagemCapa()))
                .setConteudo(ByteString.copyFrom(ebook.getConteudo()))
                .setDataEHoraAdicao(Timestamp.newBuilder()
                        .setSeconds(instant.getEpochSecond())
                        .setNanos(instant.getNano())
                        .build());

        if (ebook.getPaginaAtual() != null) {
            ebookProtoBuilder.setPaginaAtual(ebook.getPaginaAtual());
        }

        if (ebook.getSenhaDoEbook() != null) {
            ebookProtoBuilder.setSenhaDoEbook(ebook.getSenhaDoEbook());
        }

        return ebookProtoBuilder.build();
    }

    @Override
    public br.com.danielschiavo.livrariavirtual.modelo.Ebook fromProto(Ebook ebook) {
        Timestamp timestamp = ebook.getDataEHoraAdicao();
        LocalDateTime localDateTime = Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos()).atZone(ZoneId.of("America/Sao_Paulo")).toLocalDateTime();
        return br.com.danielschiavo.livrariavirtual.modelo.Ebook.builder()
                                                .nome(ebook.getNome())
                                                .nomeArquivo(ebook.getNomeArquivo())
                                                .imagemCapa(ebook.getImagemCapa().toByteArray())
                                                .conteudo(ebook.getConteudo().toByteArray())
                                                .paginaAtual(ebook.getPaginaAtual())
                                                .dataEHoraAdicao(localDateTime)
                                                .senhaDoEbook(ebook.getSenhaDoEbook()).build();
    }

    @Override
    public List<Ebook> toProto(List<br.com.danielschiavo.livrariavirtual.modelo.Ebook> ebooks) {
        return ebooks.stream().map(this::toProto).collect(Collectors.toList());
    }

    @Override
    public List<br.com.danielschiavo.livrariavirtual.modelo.Ebook> fromProto(List<Ebook> ebooksProto) {
        return ebooksProto.stream().map(this::fromProto).collect(Collectors.toList());
    }
}

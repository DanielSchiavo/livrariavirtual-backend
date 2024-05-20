package br.com.danielschiavo.livrariavirtual.infra;

import br.com.danielschiavo.livrariavirtual.aplicacao.UsuarioProtobufAdapter;
import br.com.danielschiavo.livrariavirtual.modelo.Usuario;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
        return Usuario.builder().id(UUID.fromString(usuarioProto.getId()))
                                .nome(usuarioProto.getNome())
                                .email(usuarioProto.getEmail())
                                .build();
    }
}

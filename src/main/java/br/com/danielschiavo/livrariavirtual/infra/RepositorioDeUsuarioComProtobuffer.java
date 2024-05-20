package br.com.danielschiavo.livrariavirtual.infra;

import br.com.danielschiavo.livrariavirtual.aplicacao.UsuarioProtobufAdapter;
import br.com.danielschiavo.livrariavirtual.modelo.RepositorioDeUsuario;
import br.com.danielschiavo.livrariavirtual.modelo.Usuario;
import br.com.danielschiavo.livrariavirtual.modelo.UsuarioNaoEncontradoException;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RepositorioDeUsuarioComProtobuffer implements RepositorioDeUsuario {

    private static final String caminhoRepositorio = "dados.bin";

    private UsuarioProtobufAdapter usuarioProtobufAdapter;

    public RepositorioDeUsuarioComProtobuffer(UsuarioProtobufAdapter usuarioProtobufAdapter) {
        this.usuarioProtobufAdapter = usuarioProtobufAdapter;
    }

    @Override
    public List<Usuario> pegarTodosUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(String.valueOf(caminhoRepositorio))) {
            while (fis.available() > 0) {
                br.com.danielschiavo.livrariavirtual.protos.Usuario usuarioProto = br.com.danielschiavo.livrariavirtual.protos.Usuario.parseDelimitedFrom(fis);
                if (usuarioProto != null) {
                    usuarios.add(usuarioProtobufAdapter.fromProto(usuarioProto));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao deserializar o arquivo", e);
        }
        return usuarios;
    }

    @Override
    public void cadastrarUsuario(Usuario usuario) {
        List<br.com.danielschiavo.livrariavirtual.protos.Usuario> usuariosProto = new ArrayList<>();
        // Ler todos os usuários existentes no arquivo
        try (FileInputStream fis = new FileInputStream(caminhoRepositorio)) {
            while (fis.available() > 0) {
                br.com.danielschiavo.livrariavirtual.protos.Usuario usuarioProto = br.com.danielschiavo.livrariavirtual.protos.Usuario.parseDelimitedFrom(fis);
                if (usuarioProto != null) {
                    usuariosProto.add(usuarioProto);
                }
            }
        } catch (IOException e) {
            // Se o arquivo não existe ou está vazio, podemos ignorar esse erro
        }

        // Adicionar o novo usuário à lista
        br.com.danielschiavo.livrariavirtual.protos.Usuario novoUsuarioProto = usuarioProtobufAdapter.toProto(usuario);
        usuariosProto.add(novoUsuarioProto);

        // Escrever todos os usuários (incluindo o novo) de volta para o arquivo
        try (FileOutputStream fos = new FileOutputStream(caminhoRepositorio)) {
            for (br.com.danielschiavo.livrariavirtual.protos.Usuario usuarioProto : usuariosProto) {
                usuarioProto.writeDelimitedTo(fos);
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao serializar e adicionar o usuário ao arquivo", e);
        }
    }

    @Override
    public void deletarUsuario(Usuario usuario) throws UsuarioNaoEncontradoException {
        Set<br.com.danielschiavo.livrariavirtual.protos.Usuario> usuariosProto = new HashSet<>();
        // Ler todos os usuários existentes no arquivo
        try (FileInputStream fis = new FileInputStream(caminhoRepositorio)) {
            while (fis.available() > 0) {
                br.com.danielschiavo.livrariavirtual.protos.Usuario usuarioProto = br.com.danielschiavo.livrariavirtual.protos.Usuario.parseDelimitedFrom(fis);
                if (usuarioProto != null) {
                    usuariosProto.add(usuarioProto);
                }
            }
        } catch (FileNotFoundException e) {
            throw new UsuarioNaoEncontradoException("Não foi possivel encontrar o arquivo de persistencia");
        } catch (IOException e) {
            throw new UsuarioNaoEncontradoException("Aconteceu um problema de escrita ou leitura dos dados");
        }

        // Adicionar o novo usuário à lista
        br.com.danielschiavo.livrariavirtual.protos.Usuario deletarUsuarioProto = usuarioProtobufAdapter.toProto(usuario);
        usuariosProto.add(deletarUsuarioProto);

        // Escrever todos os usuários (incluindo o novo) de volta para o arquivo
        try (FileOutputStream fos = new FileOutputStream(caminhoRepositorio)) {
            for (br.com.danielschiavo.livrariavirtual.protos.Usuario usuarioProto : usuariosProto) {
                usuarioProto.writeDelimitedTo(fos);
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao serializar e adicionar o usuário ao arquivo", e);
        }
    }
}

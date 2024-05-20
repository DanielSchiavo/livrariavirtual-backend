package br.com.danielschiavo.livrariavirtual.infra;

import br.com.danielschiavo.livrariavirtual.aplicacao.*;
import br.com.danielschiavo.livrariavirtual.modelo.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UsuarioController {

    private final GeradorUUID geradorUUID;
    private final UsuarioMapper usuarioMapper;
    private final UsuarioProtobufAdapter usuarioProtobufAdapter;
    private final RepositorioDeUsuarioComProtobuffer repositorioDeUsuarioComProtobuffer;

    @Autowired
    public UsuarioController(GeradorUUID geradorUUID, UsuarioMapper usuarioMapper, UsuarioProtobufAdapter usuarioProtobufAdapter, RepositorioDeUsuarioComProtobuffer repositorioDeUsuarioComProtobuffer) {
        this.geradorUUID = geradorUUID;
        this.usuarioMapper = usuarioMapper;
        this.usuarioProtobufAdapter = usuarioProtobufAdapter;
        this.repositorioDeUsuarioComProtobuffer = repositorioDeUsuarioComProtobuffer;
    }

    @PostMapping("/usuario")
    public ResponseEntity<?> cadastrarUsuario(@RequestBody CadastrarUsuarioDTO cadastrarUsuarioDTO) {
        String respostaCadastrarUsuario = new CadastrarUsuario(repositorioDeUsuarioComProtobuffer, geradorUUID, usuarioMapper).executa(cadastrarUsuarioDTO);

        return ResponseEntity.ok(respostaCadastrarUsuario);
    }

    @GetMapping("/usuario")
    public ResponseEntity<?> pegarTodosUsuarios() {
        List<Usuario> usuarios = new PegarTodosUsuarios(repositorioDeUsuarioComProtobuffer).executa();

        return ResponseEntity.ok(usuarios);
    }
}

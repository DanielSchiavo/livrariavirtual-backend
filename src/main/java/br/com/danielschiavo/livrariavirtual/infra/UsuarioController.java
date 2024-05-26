package br.com.danielschiavo.livrariavirtual.infra;

import br.com.danielschiavo.livrariavirtual.aplicacao.*;
import br.com.danielschiavo.livrariavirtual.dto.AdicionarEbookDTO;
import br.com.danielschiavo.livrariavirtual.dto.CadastrarUsuarioDTO;
import br.com.danielschiavo.livrariavirtual.dto.RespostaAdicionarEbookDTO;
import br.com.danielschiavo.livrariavirtual.modelo.Ebook;
import br.com.danielschiavo.livrariavirtual.modelo.Usuario;
import br.com.danielschiavo.livrariavirtual.modelo.UsuarioNaoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RequestMapping("/livrariavirtual/usuario")
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

    @GetMapping("/verificar")
    public ResponseEntity<?> verificarSeUsuarioExiste() {
        try {
            Usuario usuario = new PegarUsuario(repositorioDeUsuarioComProtobuffer).executa();
            return ResponseEntity.ok().body(true);
        } catch (UsuarioNaoEncontradoException e) {
            return ResponseEntity.ok().body(false);
        }
    }

    @PostMapping
    public ResponseEntity<?> cadastrarUsuario(
            @RequestBody CadastrarUsuarioDTO cadastrarUsuarioDTO
    ) {
        Usuario respostaCadastrarUsuario = new CadastrarUsuario(repositorioDeUsuarioComProtobuffer, geradorUUID, usuarioMapper).executa(cadastrarUsuarioDTO);

        return ResponseEntity.ok(respostaCadastrarUsuario);
    }

    @GetMapping
    public ResponseEntity<?> pegarUsuario() {
        try {
            Usuario usuario = new PegarUsuario(repositorioDeUsuarioComProtobuffer).executa();
            return ResponseEntity.ok(usuario);
        } catch (UsuarioNaoEncontradoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{usuarioId}/ebook")
    public ResponseEntity<?> adicionarEbooks(@RequestBody List<AdicionarEbookDTO> ebooksDTO, @PathVariable UUID usuarioId) {
        RespostaAdicionarEbookDTO respostaAdicionarEbooks = new AdicionarEbooks(repositorioDeUsuarioComProtobuffer, geradorUUID, usuarioMapper).executa(ebooksDTO, usuarioId);

        return ResponseEntity.ok().body(respostaAdicionarEbooks);
    }

    @GetMapping("/{usuarioId}/ebook/{ebooksId}")
    public ResponseEntity<?> pegarEbookPorId(@PathVariable List<UUID> ebooksId, @PathVariable UUID usuarioId) {
        List<Ebook> respostaPegarEbookPorId = new PegarEbookPorId(repositorioDeUsuarioComProtobuffer).executa(ebooksId, usuarioId);

        return ResponseEntity.ok().body(respostaPegarEbookPorId);
    }
}

package br.com.danielschiavo.livrariavirtual.aplicacao;

import br.com.danielschiavo.livrariavirtual.dto.AdicionarEbookDTO;
import br.com.danielschiavo.livrariavirtual.dto.RespostaAdicionarEbookDTO;
import br.com.danielschiavo.livrariavirtual.modelo.Ebook;
import br.com.danielschiavo.livrariavirtual.modelo.RepositorioDeUsuario;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class AdicionarEbooks {

    private RepositorioDeUsuario repositorioUsuario;

    private GeradorUUID geradorUUID;

    private UsuarioMapper usuarioMapper;

    public AdicionarEbooks(RepositorioDeUsuario repositorioDeUsuario, GeradorUUID geradorUUID, UsuarioMapper usuarioMapper) {
        this.repositorioUsuario = repositorioDeUsuario;
        this.geradorUUID = geradorUUID;
        this.usuarioMapper = usuarioMapper;
    }

    public RespostaAdicionarEbookDTO executa(List<AdicionarEbookDTO> ebooksDTO, UUID usuarioId) {
        List<Ebook> ebooksSemSenha = new ArrayList<>();
        List<Ebook> ebooksComSenha = new ArrayList<>();

        ebooksDTO.forEach(ebookDTO -> {
            Ebook.EbookBuilder ebookBuilder = Ebook.builder().nomeArquivo(ebookDTO.getNomeArquivo())
                                                            .conteudo(ebookDTO.getConteudo());

            try (PDDocument document = Loader.loadPDF(ebookDTO.getConteudo())) {
                String nome = Arrays.stream(ebookDTO.getNomeArquivo().split("\\.")).findFirst().get();
                byte[] bytesImagemCapa = pegarImagemCapaEbook(ebookDTO, document);

                UUID ebookId = geradorUUID.gerar();

                ebookBuilder.imagemCapa(bytesImagemCapa)
                            .id(ebookId)
                            .nome(nome)
                            .dataEHoraAdicao(LocalDateTime.now())
                            .senhaDoEbook(ebookDTO.getSenha()).build();

                ebooksSemSenha.add(ebookBuilder.build());
            } catch (InvalidPasswordException e) {
                ebooksComSenha.add(ebookBuilder.build());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        repositorioUsuario.adicionarEbook(ebooksSemSenha, usuarioId.toString());

        return new RespostaAdicionarEbookDTO(ebooksSemSenha, ebooksComSenha);
    }

    private byte[] pegarImagemCapaEbook(AdicionarEbookDTO ebookDTO, PDDocument document) throws IOException {
        PDFRenderer pdfRenderer = new PDFRenderer(document);

        int pageNumber = 0;
        BufferedImage image = pdfRenderer.renderImage(pageNumber);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);

        return baos.toByteArray();
    }
}
